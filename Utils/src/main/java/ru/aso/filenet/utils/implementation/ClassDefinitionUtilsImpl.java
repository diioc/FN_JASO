package ru.aso.filenet.utils.implementation;

import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.admin.LocalizedString;
import com.filenet.api.admin.PropertyDefinition;
import com.filenet.api.collection.ClassDefinitionSet;
import com.filenet.api.collection.LocalizedStringList;
import com.filenet.api.collection.PropertyDefinitionList;
import com.filenet.api.constants.FilteredPropertyType;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.PropertyFilter;
import org.apache.log4j.Logger;
import ru.aso.filenet.utils.ClassDefinitionUtils;
import ru.aso.filenet.utils.domain.P8ClassModel;
import ru.aso.filenet.utils.domain.P8PropertyModel;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ASolynin
 * Date: 21.03.13
 * Time: 18:15
 * To change this template use File | Settings | File Templates.
 */
class ClassDefinitionUtilsImpl implements ClassDefinitionUtils {

    private static final Logger log = Logger.getLogger(ClassDefinitionUtilsImpl.class);
    private ClassDefinition definition;

    public void setDefinition(ClassDefinition definition) {
        this.definition = definition;
    }

    @Override
    public List<ClassDefinition> getAllChildren(ClassDefinition classDefinition) {
        this.definition = classDefinition;

        ArrayList<ClassDefinition> allDefinitions = new ArrayList<ClassDefinition>();
        ArrayList<ClassDefinition> currentDefinitions = new ArrayList<ClassDefinition>();
        ArrayList<ClassDefinition> nextDefinitions = new ArrayList<ClassDefinition>();

        nextDefinitions.add(definition);

        while(!nextDefinitions.isEmpty()) {

            log.trace("Next iteration of found subclasses.");
            allDefinitions.addAll(nextDefinitions);
            log.trace("Now we have " + allDefinitions.size() + " subclasses.");

            currentDefinitions.clear();
            currentDefinitions.addAll(nextDefinitions);
            nextDefinitions.clear();

            for (ClassDefinition def : currentDefinitions) {

                log.trace("Next definition parse.");
                def.fetchProperties(new String[]{PropertyNames.IMMEDIATE_SUBCLASS_DEFINITIONS});
                ClassDefinitionSet definitionSet = def.get_ImmediateSubclassDefinitions();
                if (!definitionSet.isEmpty()) {
                    Iterator<ClassDefinition> iterator = definitionSet.iterator();
                    while (iterator.hasNext()) {
                        nextDefinitions.add(iterator.next());
                    }
                }
            }
        }

        allDefinitions.remove(0);
        return allDefinitions;
    }

    @Override
    public P8ClassModel getModel(ClassDefinition classDefinition, boolean includeInheritedProps) {

        PropertyFilter propertyFilter = new PropertyFilter();
        propertyFilter.addIncludeType(0, null, Boolean.TRUE, FilteredPropertyType.ANY, null);
        classDefinition.fetchProperties(propertyFilter);

        P8ClassModel classModel = new P8ClassModel();
        classModel.setId(classDefinition.get_Id().toString());
        classModel.setDescription(classDefinition.get_DescriptiveText());
        classModel.setDisplayName(classDefinition.get_DisplayName());
        log.trace("Parsing class '" + classModel.getDisplayName() + "'");
        ClassDefinition parentClassDefinition = classDefinition.get_SuperclassDefinition();
        parentClassDefinition.fetchProperties(new String[] {PropertyNames.SYMBOLIC_NAME});
        classModel.setParentClassSymbolicName(parentClassDefinition.get_SymbolicName());
        classModel.setSymbolicName(classDefinition.get_SymbolicName());

        PropertyDefinitionList propertyDefinitions = classDefinition.get_PropertyDefinitions();
        Iterator<PropertyDefinition> propDefinitionIter = propertyDefinitions.iterator();
        List<P8PropertyModel> propertiesList = new ArrayList<P8PropertyModel>(propertyDefinitions.size());
        Set<String> scpropsNames = null;

        if (!includeInheritedProps) {
            log.trace("Getting class definitions without inherited properties.");
            ClassDefinition superclassDefinition = classDefinition.get_SuperclassDefinition();
            superclassDefinition.fetchProperties(propertyFilter);
            PropertyDefinitionList superclassDefinitionList = superclassDefinition.get_PropertyDefinitions();
            log.trace("Superclass have " + superclassDefinitionList.size() + "properties.");
            scpropsNames = new HashSet<String>(superclassDefinitionList.size());
            Iterator<PropertyDefinition> scPropertyDefinitionIter = superclassDefinitionList.iterator();
            while (scPropertyDefinitionIter.hasNext()) {
                PropertyDefinition scPropDefinition = scPropertyDefinitionIter.next();
                scpropsNames.add(scPropDefinition.get_SymbolicName());
            }
        }

        while (propDefinitionIter.hasNext()) {
            PropertyDefinition propertyDefinition = propDefinitionIter.next();
            if (includeInheritedProps) {
                if (!propertyDefinition.get_IsSystemOwned()) {
                    P8PropertyModel propertyModel = UFactory.property.getModel(propertyDefinition);
                    propertiesList.add(propertyModel);
                }
            }
            else {
                if (scpropsNames == null) log.error("Property definitions list of superclass is null.");
                if (!propertyDefinition.get_IsSystemOwned() && !scpropsNames.contains(propertyDefinition.get_SymbolicName())) {
                    P8PropertyModel propertyModel = UFactory.property.getModel(propertyDefinition);
                    propertiesList.add(propertyModel);
                }
            }
        }
        classModel.setProperties(propertiesList);

        return classModel;
    }

    @Override
    public String createClassDefinition(ObjectStore os, P8ClassModel classModel, boolean overwriteIfExist) {
        String parentClassDefinition = classModel.getParentClassSymbolicName();
        ClassDefinition baseClassDefinition = Factory.ClassDefinition.fetchInstance(os, parentClassDefinition, null);
        ClassDefinition newClassDefinition = baseClassDefinition.createSubclass();

        newClassDefinition.set_SymbolicName(classModel.getSymbolicName());

        LocalizedStringList localStrList = Factory.LocalizedString.createList();
        LocalizedString localString = Factory.LocalizedString.createInstance(os);
        localString.set_LocaleName("ru");
        localString.set_LocalizedText(classModel.getDisplayName());
        newClassDefinition.set_DisplayNames(localStrList);

        localStrList = Factory.LocalizedString.createList();
        localString = Factory.LocalizedString.createInstance(os);
        localString.set_LocaleName("ru");
        localString.set_LocalizedText(classModel.getDescription());
        newClassDefinition.set_DescriptiveTexts(localStrList);

        newClassDefinition.save(RefreshMode.REFRESH);

        newClassDefinition.fetchProperties(new String[]{PropertyNames.PROPERTY_DEFINITIONS});
        PropertyDefinitionList propList = newClassDefinition.get_PropertyDefinitions();
        return null;
    }

    @Override
    public void addProperty(ClassDefinition classDefinition, P8PropertyModel propertyModel) {
        //TODO
    }

    @Override
    public void removeProperty(ClassDefinition classDefinition, P8PropertyModel propertyModel) {
        //TODO
    }

    @Override
    public boolean isExist(ClassDefinition classDefinition) {
        //TODO
        return false;
    }
}
