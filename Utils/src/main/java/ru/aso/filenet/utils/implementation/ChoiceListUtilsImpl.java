package ru.aso.filenet.utils.implementation;

import com.filenet.api.admin.*;
import com.filenet.api.constants.*;
import com.filenet.api.core.Factory;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.RepositoryRow;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;
import org.apache.log4j.Logger;
import ru.aso.filenet.utils.ChoiceListUtils;
import ru.aso.filenet.utils.domain.DeletedEntity;
import ru.aso.filenet.utils.domain.P8ChoiceListModel;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/**
 * User: ASO
 * Date: 22.05.13
 * Time: 0:04
 * Version 0.3.2
 *
 * This is base implementation of ChoiceListUtils interface for work with CE object - ChoiceList.
 * It gives methods
 * for create DOM (P8ChoiceListModel) of ChoiceList,
 * for create ChoiceList using DOM and
 * for delete ChoiceList from object store using DOM model.
 */
class ChoiceListUtilsImpl implements ChoiceListUtils {
    private static final Logger logger = Logger.getLogger(ChoiceListUtilsImpl.class);
    private static ObjectStore os = null;

    /**
     * Create model of the ChoiceList object.
     * @param choiceList object
     * @return DOM from Choice list object
     */
    @Override
    public P8ChoiceListModel getModel(ChoiceList choiceList) {
        try {
            /*Fetching properties.*/
            PropertyFilter pf = new PropertyFilter();
            FilterElement fe = new FilterElement(0, null, true, FilteredPropertyType.ANY, null);
            pf.addIncludeType(fe);
            choiceList.fetchProperties(pf);
            logger.trace("Creating model object for ChoiceList " + choiceList.get_DisplayName());
            /**
            * After fetching properties we are create new P8ChoiceListModel object and set its fields.
            * Also we create Properties object which using for presenting fn objects Choice.
            */
            P8ChoiceListModel choiceListModel = new P8ChoiceListModel();
            choiceListModel.setDisplayName(choiceList.get_DisplayName());
            choiceListModel.setTypeAsInt(choiceList.get_DataType().getValue());
            choiceListModel.setDescription(choiceList.get_DescriptiveText());
            choiceListModel.setId(choiceList.get_Id().toString());
            Iterator<Choice> chListIterator = choiceList.get_ChoiceValues().iterator();
            Properties choicePairs = new Properties();
            while (chListIterator.hasNext()) {
                Choice choice = chListIterator.next();
                choicePairs.setProperty(choice.get_Name(), getChoiceValue(choice));
            }
            choiceListModel.setChoiceItems(choicePairs);
            /**
            * Here using choiceListModelString() service method for generate String which describes
            * choiceList DOM object. Logging its String and return DOM object.
            */
            logger.info("Successfully get " + choiceListModelString(choiceListModel));
            return choiceListModel;
        }
        catch (Exception e) {
            logger.error("Exception occurs while creating choiceList model.", e);
            return null;
        }
    }

    /**
     * Method that create new choice list or recreate it's if choice list already existing.
     * @param os - ObjectStore
     * @param choiceListModel DOM from Choice list object
     * @param overwriteIfExist it's flag means that choice list must be overwritten if it exists.
     */
    @Override
    public synchronized void createChoiceList(ObjectStore os, P8ChoiceListModel choiceListModel, boolean overwriteIfExist) {
        this.os = os;
        os.fetchProperties(new String[]{PropertyNames.DISPLAY_NAME});
        logger.trace("Try to create new choice list in object store " + os.get_DisplayName());
        /**
         * Use isExist() service method for search choiceList in object store. If choice list already exists, when
         * check overwriteIfExist flag. If flag value is set to FALSE when return null, if flag set to TRUE  or
         * isExist() return FALSE when
         * invoke pDeleteChoiceList() service method for delete its choice list, but persist all relationships
         * between choice lists and property templates/definitions.
         * Next step is invoke internal method createNewChoiceList(), which create choice list in object store and
         * restores all relationships.
         */
        try {
            if (isExist(choiceListModel) == Boolean.TRUE) {
                logger.trace("Choice list " + choiceListModel.getDisplayName() + " already exists in object store.");
                if (overwriteIfExist == Boolean.FALSE) return;
                else {
                    logger.trace("Overwriting existing choice list.");
                    pDeleteChoiceList(choiceListModel);
                }
            }
            createNewChoiceList(choiceListModel);
            logger.info("Successfully create choice list " + choiceListModel.getDisplayName() + " Id = " + choiceListModel.getId());
        }
        catch (Exception e) {
            logger.error("Exception occurs while creating choiceList.", e);
        }
    }

    /**
     * This method for deletion choice list from object store.
     * @param os ObjectStore
     * @param model P8ChoiceListModel is DOM for choice list
     */
    public synchronized void deleteChoiceList(ObjectStore os, P8ChoiceListModel model) {
        this.os = os;
        try {
            ChoiceList chList = Factory.ChoiceList.getInstance(os, new Id(model.getId()));
            /**
             * Using propertyUnlink() service method with second FALSE parameter for
             * destroy all choiceList relationships without persistence.
             */
            propertiesUnlink(chList, false);
            chList.delete();
            chList.save(RefreshMode.REFRESH);
            logger.info("Choice list " + model.getDisplayName() + " has been successfully deleted.");
        } catch (EngineRuntimeException e) {
            logger.error("Error while deleting choice list.", e);
        }
    }

    /***********************************************************************************/
    /*Next code block present private internal methods that service main public methods*/
    /***********************************************************************************/

    /**
     * Search for choiceList in object store.
     * @param choiceListModel P8ChoiceListModel is DOM for choice list
     * @return boolean value. True if choice list already exists.
     */
    private boolean isExist(P8ChoiceListModel choiceListModel) {
        /*Preparing parameters for choice list searching*/
        String id = choiceListModel.getId();
        if (id == null) id = "{00000000-0000-0000-0000-000000000000}";
        String name = choiceListModel.getDisplayName();
        if (name == null) name = "";

        /*Preparing statement*/
        SearchScope scope = new SearchScope(os);
        SearchSQL sql = new SearchSQL("SELECT id FROM ChoiceList cl WHERE cl.DisplayName = '" + name +
            "' OR cl.Id = '" + id + "'");
        logger.trace("Checking choice list existence. Execute query: " + sql.toString());
        /**
         * Performing query and returning result
         */
        return !scope.fetchObjects(sql, null, null, null).isEmpty();
    }

    /**
     * Delete the choice list. This method must be private because it invokes from other public deletion method: deleteChoiceList.
     * It's delete choice list and push all its relationships between and property templates and definitions in special structure
     * for next usage.
     * @param model P8ChoiceListModel is DOM for choice list
     */
    private void pDeleteChoiceList(P8ChoiceListModel model) {
        /**
         * Getting choiceList from object store and begin deletion procedure.
         */
        Id choiceListId;
        if (model.getId() != null)
            choiceListId = new Id(model.getId());
        else
            choiceListId = getChoiceListId(model);
        ChoiceList chList = Factory.ChoiceList.getInstance(os, choiceListId);
        logger.trace("Start process deletion for choice list " + model.getDisplayName());
        /**
         * For delete all relationships between choiceList and properties (template/definitions), also persist
         * these relationships using method propertyUnlink() with second TRUE parameter.
         */
        propertiesUnlink(chList, true);
        chList.delete();
        chList.save(RefreshMode.REFRESH);
        logger.trace("Delete choiceList " + model.getDisplayName());
    }

    /**
     * Create new choice list. This method must be private because it invokes from other public creation method: createChoiceList().
     * @param chListModel P8ChoiceListModel is DOM for choice list
     */
    private void createNewChoiceList(P8ChoiceListModel chListModel) {
        /**
         * Create choice list and set some properties. If model has Id when use it, if not when create with new Id.
         */
        ChoiceList choiceList;
        if (chListModel.getId() != null)
            choiceList = Factory.ChoiceList.createInstance(os, new Id(chListModel.getId()));
        else {
            choiceList = Factory.ChoiceList.createInstance(os);
            /*Set new choiceList id to choiceList model*/
        }
        choiceList.set_DisplayName(chListModel.getDisplayName());
        choiceList.set_DescriptiveText(chListModel.getDescription());
        choiceList.set_DataType(TypeID.getInstanceFromInt(chListModel.getTypeAsInt()));
        choiceList.set_ChoiceValues(Factory.Choice.createList());
        /**
         * Creating choices and add theirs to choice list. Here using setChoiceType() and setChoiceValue()
         * service methods for comfortable work with choice types.
         */
        Set<String> choiceNames = chListModel.getChoiceItems().stringPropertyNames();
        for (String choiceName : choiceNames) {
            Choice choice = Factory.Choice.createInstance();
            setChoiceType(choice, chListModel.getTypeAsInt());
            choice.set_DisplayName(choiceName);
            setChoiceValue(choice, chListModel.getChoiceItems().getProperty(choiceName));
            choiceList.get_ChoiceValues().add(choice);
        }
        choiceList.save(RefreshMode.NO_REFRESH);
        if (chListModel.getId() == null) {
            choiceList.fetchProperties(new String[]{PropertyNames.ID});
            chListModel.setId(choiceList.get_Id().toString());
        }
        /**
         * Search for choiceList relationships in deletedEntity structure.
         * And restore its relationships by restoreLinks() service method if they exists.
         */
        if (UFactory.deletedEntity.hasEntity(chListModel.getDisplayName(), DeletedEntity.CHOICELIST)) {
            logger.trace("Choice list " + chListModel.getDisplayName() + " persisted relationships was found.");
            Set<String> unlinkedIdSet = UFactory.deletedEntity.pullLinkedObjects(chListModel.getDisplayName(), DeletedEntity.CHOICELIST);
            restoreLinks(unlinkedIdSet, choiceList);
        }
    }

    /**
     * Internal method for link choice list into properties. For that using relationships persisted in
     * deletedEntity structure.
     * @param entitiesId Set of entities ID in either of two formats
     * {PROPERTY_TEMPLATE_ID} if its entity present property template relationship or
     * {PROPERTY_TEMPLATE_ID|CLASS_DEFINITION_ID} if its entity present property definition.
     * Set must consists all entities in which need to restore links on choiceList
     * @param choiceList ChoiceList is DOM for choice list.
     */
    private void restoreLinks(Set<String> entitiesId, ChoiceList choiceList) {
        logger.trace("Start restore links procedure.");
        for (String entityId : entitiesId) {
            /*In which case working with property definition*/
            if (entityId.contains("|")) {
                /**
                * Need parsing entityId and link choiceList to property definition.
                * PropertyTemplate ID = idParts[0]
                * ClassDefinition ID = idParts[1]
                */
                String idParts[] = entityId.split("\\|");
                logger.trace("Adding choice list to property definition with template_id = " + idParts[0] +
                    " and class_definition_id = " + idParts[1]);
                /**
                 * Getting propertyTemplate using id (entity second part) for discovering property symbolic name
                 */
                PropertyTemplate template = Factory.PropertyTemplate.getInstance(os, new Id(idParts[0]));
                template.fetchProperties(new String[]{PropertyNames.SYMBOLIC_NAME});
                String propertyName = template.get_SymbolicName();
                /**
                 * Getting class definition in which the property need link choiceList.
                 */
                ClassDefinition classDefinition = Factory.ClassDefinition.getInstance(os, new Id(idParts[1]));
                PropertyFilter pf = new PropertyFilter();
                pf.addIncludeType(0, null, true, FilteredPropertyType.LIST_OF_OBJECT, null);
                pf.addIncludeProperty(0, null, true, PropertyNames.SYMBOLIC_NAME, null);
                classDefinition.fetchProperties(pf);
                Iterator<PropertyDefinition> properties = classDefinition.get_PropertyDefinitions().iterator();
                /**
                 * Iterate every property definition until find the right.
                 */
                while (properties.hasNext()) {
                    PropertyDefinition property = properties.next();
                    /*Compare property definition name with name received earlier from property template */
                    if (property.get_SymbolicName().equals(propertyName)) {
                        property.set_ChoiceList(choiceList);
                        logger.info("Successfully link choiceList to property " + property.get_SymbolicName() + " of class " + classDefinition.get_SymbolicName());
                    }
                }
                classDefinition.save(RefreshMode.NO_REFRESH);
            }
            /*In which case working with property template*/
            else {
                /**
                 * This entity not parsed.
                 * Just add choiceList to property template with it id.
                 */
                logger.trace("Adding choice list to property template with id = " + entityId);
                PropertyTemplate template = Factory.PropertyTemplate.getInstance(os, new Id(entityId));
                template.set_ChoiceList(choiceList);
                template.save(RefreshMode.NO_REFRESH);
                logger.info("Successfully link choiceList to propertyTemplate.");
            }
        }
        logger.trace("Finish of restoring links procedure.");
    }

    /**
     * Delete relationships of choiceList from property template and definitions, and persist theirs
     * in deletedEntity structure by using LinkPersistenceProvider interface.
     * @param choiceList ChoiceList.
     * @param persistRelationships if set TRUE when all choiceList relationships will be persist in deletedEntity structure.
     */
    private void propertiesUnlink(ChoiceList choiceList, boolean persistRelationships) {
        /**
         * First handle all PropertyTemplate objects in system. Searching among them templates with choiceList that
         * matches P8ChoiceListMode in argument, unlink its form template and persist if flag set in TRUE.
         */
        if (choiceList.getProperties().isPropertyPresent(PropertyNames.ID) == Boolean.FALSE ||
            choiceList.getProperties().isPropertyPresent(PropertyNames.DISPLAY_NAME) == Boolean.FALSE) {
            choiceList.fetchProperties(new String[]{PropertyNames.ID, PropertyNames.DISPLAY_NAME});
        }
        if (choiceList.getProperties().get(PropertyNames.ID).getState() != PropertyState.VALUE ||
            choiceList.getProperties().get(PropertyNames.DISPLAY_NAME).getState() != PropertyState.VALUE)
            choiceList.fetchProperties(new String[]{PropertyNames.ID, PropertyNames.DISPLAY_NAME});
        logger.trace("Start unlink procedure for choice list " + choiceList.get_DisplayName());
        SearchScope scope = new SearchScope(os);
        SearchSQL sql = new SearchSQL("SELECT * FROM PropertyTemplate WITH INCLUDESUBCLASSES");
        PropertyFilter pf = new PropertyFilter();
        pf.addIncludeProperty(new FilterElement(null, null, false, PropertyNames.CHOICE_LIST + " "
                + PropertyNames.SYMBOLIC_NAME + " "
                + PropertyNames.ID, null));
        Iterator<IndependentObject> objectIterator = scope.fetchObjects(sql, 500, pf, true).iterator();
        /**
         * Iterate every template.
         */
        while (objectIterator.hasNext()) {
            PropertyTemplate propertyTemplate = (PropertyTemplate)objectIterator.next();
            /**
             * Checking template
             */
            if (propertyTemplate.get_ChoiceList() != null) {
                if (propertyTemplate.get_ChoiceList().get_Id().toString().equals(choiceList.get_Id().toString())) {
                    propertyTemplate.set_ChoiceList(null);
                    propertyTemplate.save(RefreshMode.NO_REFRESH);
                    logger.info("Unlink choiceList " + choiceList.get_DisplayName() + " from property template "
                            + propertyTemplate.get_SymbolicName());
                    /*Persisting link to this PropertyTemplate*/
                    if (persistRelationships == Boolean.TRUE)
                        UFactory.deletedEntity.pushDeletedEntity(choiceList.get_DisplayName(),
                                DeletedEntity.CHOICELIST, propertyTemplate.get_Id().toString());
                }
            }
        }
        /**
         * Second handle all PropertyDefinitions in system like handle all PropertyTemplates.
         */
        pf = new PropertyFilter();
        pf.addIncludeType(0, null, true, FilteredPropertyType.LIST_OF_OBJECT, null);
        pf.addIncludeProperty(1, null, true, PropertyNames.ID + " " + PropertyNames.CHOICE_LIST + " " + PropertyNames.PROPERTY_TEMPLATE, null);
        sql = new SearchSQL("SELECT * FROM ClassDefinition");
        Iterator<ClassDefinition> definitionsIter = scope.fetchObjects(sql, 500, pf, true).iterator();
        /**
         * Iterate every ClassDefinition.
         */
        while (definitionsIter.hasNext()) {
            ClassDefinition classDefinition = definitionsIter.next();
            /**
             * boolean dirtyClass flag need to mark (set TRUE) if classDefinition was modified and would be saved.
             * It's may occur if choiceList was unlinked from on of the propertyDefinitions of this classDefinition.
             */
            boolean dirtyClass = false;
            Iterator<PropertyDefinition> propIter = classDefinition.get_PropertyDefinitions().iterator();
            /**
             * Iterate every PropertyDefinition in current ClassDefinition for searching choiceList that matches
             * choiceListModel.
             */
            while (propIter.hasNext()) {
                PropertyDefinition property = propIter.next();
                ChoiceList list = property.get_ChoiceList();
                if (list == null) continue;
                if (list.get_Id().toString().equals(choiceList.get_Id().toString())) {
                    property.set_ChoiceList(null);
                    dirtyClass = true;
                    logger.trace("Unlink choiceList from property definition " +
                            property.get_PropertyTemplate().get_Id() + " of class " + classDefinition.get_Id());
                    /**
                     * Persisting link to this PropertyDefinition that presented like pair of
                     * PropertyTemplateID and ClassDefinitionID separated by '|'
                     */
                    if (persistRelationships)
                        UFactory.deletedEntity.pushDeletedEntity(choiceList.get_DisplayName(), DeletedEntity.CHOICELIST,
                                property.get_PropertyTemplate().get_Id().toString() + "|" + classDefinition.get_Id().toString());
                }
            }
            if (dirtyClass)
                classDefinition.save(RefreshMode.NO_REFRESH);
        }
        logger.trace("Finish of unlink procedure.");
    }

    /**
     * Method for set choiceList Id in P8ChoiceListModel which method takes as argument. For fetching Id method use
     * choiceList DisplayName from argument.
     * @param model P8ChoiceListModel is DOM for choice list.
     */
    private Id getChoiceListId(P8ChoiceListModel model) {
        if (model.getDisplayName() == null)
            throw new IllegalArgumentException("Exception occurs in setChoiceListId() method. ChoiceList name is NULL, but " +
                    "it must have not null value.");
        SearchScope scope = new SearchScope(os);
        SearchSQL sql = new SearchSQL("SELECT Id FROM ChoiceList WHERE DisplayName = '" + model.getDisplayName() + "'");
        Iterator<RepositoryRow> rowIterator = scope.fetchRows(sql, null, null, false).iterator();
        while (rowIterator.hasNext())
            return rowIterator.next().getProperties().get("Id").getIdValue();
        throw new RuntimeException("Search choiceList id exception.");
    }

    /**
     * Service method for getting value of Choice object.
     * @param choice Choice
     * @return Choice value as String
     */
    private String getChoiceValue(Choice choice) {
        if (choice.get_ChoiceType() == ChoiceType.INTEGER)
            return choice.get_ChoiceIntegerValue().toString();
        else if (choice.get_ChoiceType() == ChoiceType.STRING)
            return choice.get_ChoiceStringValue();
        else
            throw new RuntimeException("Unsupported choice type " + choice.get_ChoiceType());
    }

    /**
     * Service method for setting Choice object value
     * @param choice Choice
     * @param value String
     */
    private void setChoiceValue (Choice choice, String value) {
        if (choice.get_ChoiceType() == ChoiceType.INTEGER)
            choice.set_ChoiceIntegerValue(Integer.valueOf(value));
        else if (choice.get_ChoiceType() == ChoiceType.STRING)
            choice.set_ChoiceStringValue(value);
        else
            throw new RuntimeException("Unexpected choice type " + choice.get_ChoiceType());
    }

    /**
     * Service method for set type of Choice object. It's converts TypeID value to ChoiceType value.
     * @param choice Choice
     * @param typeID TypeID as INT
     */
    private void setChoiceType (Choice choice, int typeID) {
        if (typeID == TypeID.STRING_AS_INT)
            choice.set_ChoiceType(ChoiceType.STRING);
        else if (typeID == TypeID.LONG_AS_INT)
            choice.set_ChoiceType(ChoiceType.INTEGER);
        else
            throw new RuntimeException("Unexpected choiceList type " + typeID);
    }

    /**
     * Service method for generate ChoiceList to String by template: "Choice list: NAME with entries: \n
     * choiceName - choiceValue
     * ... for every choice". It uses for logging.
     * @param model P8ChoiceListModel is DOM for choice list.
     * @return String
     */
    private String choiceListModelString(P8ChoiceListModel model) {
        Properties choicePairs = model.getChoiceItems();
        Set<String> choiceNames = choicePairs.stringPropertyNames();
        String choiceListString = "ChoiceList " + model.getDisplayName() + " with entries:\n";
        for (String choiceName : choiceNames) {
            choiceListString += choiceName;
            choiceListString += " - ";
            choiceListString += choicePairs.getProperty(choiceName);
            choiceListString += "\n";
        }
        return choiceListString;
    }
}
