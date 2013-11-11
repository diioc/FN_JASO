package ru.aso.filenet.utils.implementation;

import com.filenet.api.admin.ChoiceList;
import com.filenet.api.admin.PropertyDefinition;
import com.filenet.api.admin.PropertyTemplate;
import com.filenet.api.constants.FilteredPropertyType;
import com.filenet.api.constants.TypeID;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.PropertyFilter;
import org.apache.log4j.Logger;
import ru.aso.filenet.utils.PropertyUtils;
import ru.aso.filenet.utils.domain.P8PropertyModel;

/**
 * User: ASO
 * Date: 22.05.13
 * Time: 0:03
 */
class PropertyUtilsImpl implements PropertyUtils {
    private static final Logger logger = Logger.getLogger(PropertyUtilsImpl.class);

    @Override
    public P8PropertyModel getModel(PropertyTemplate propertyTemplate) {
        return null;
    }

    @Override
    public P8PropertyModel getModel(PropertyDefinition propertyDefinition) {
        try {
            P8PropertyModel propertyModel = new P8PropertyModel();
            propertyModel.setDisplayName(propertyDefinition.get_DisplayName());
            logger.trace("Parsing definition name = " + propertyDefinition.get_DisplayName());
            propertyModel.setSymbolicName(propertyDefinition.get_SymbolicName());
            propertyModel.setDescription(propertyDefinition.get_DescriptiveText());
            int typeCode = propertyDefinition.get_DataType().getValue();
            String typeString = propertyDefinition.get_DataType().toString();
            propertyModel.setTypeAsInt(typeCode);
            propertyModel.setTypeAsString(typeString);
            propertyModel.setCardinalityAsString(propertyDefinition.get_Cardinality().toString());
            propertyModel.setCardinalityAsInt(propertyDefinition.get_Cardinality().getValue());
            propertyModel.setRequired(propertyDefinition.get_IsValueRequired());
            propertyModel.setHidden(propertyDefinition.get_IsHidden());
            ChoiceList chList = propertyDefinition.get_ChoiceList();
            if (chList != null) {
                propertyModel.setChoiceListModel(UFactory.choiceList.getModel(chList));
                propertyModel.setChoiceList(true);
            }
            else propertyModel.setChoiceList(false);

            PropertyTemplate propertyTemplate = propertyDefinition.get_PropertyTemplate();
            if (propertyTemplate != null) {
                PropertyFilter pf = new PropertyFilter();
                FilterElement filterElement = new FilterElement(0, null, false, FilteredPropertyType.ANY, null);
                pf.addIncludeType(filterElement);
                propertyTemplate.fetchProperties(pf);
                if (propertyTemplate.get_DataType() == TypeID.STRING) {
                    try {
                        int maxValueProperty = propertyTemplate.getProperties().getInteger32Value("MaximumLengthString");
                        propertyModel.setMaxSize(maxValueProperty);
                    }
                    catch (NullPointerException e) {
                        propertyModel.setMaxSize(0);
                        logger.debug("MaximumLengthString of property is empty.", e);
                    }
                    try {propertyModel.setDefaultValue(propertyDefinition.getProperties().getStringValue("PropertyDefaultString"));}
                    catch (NullPointerException e) {logger.debug("PropertyDefaultString of property is empty.", e);}

                }
                else if (propertyDefinition.get_DataType() == TypeID.LONG) {
                    try {
                        int maxValueProperty = propertyTemplate.getProperties().getInteger32Value("PropertyMaximumInteger32");
                        propertyModel.setMaxSize(maxValueProperty);
                    }
                    catch (NullPointerException e) {
                        propertyModel.setMaxSize(0);
                        logger.debug("PropertyMaximumInteger32 of property is empty.", e);
                    }
                    try {propertyModel.setDefaultValue(propertyDefinition.getProperties().getInteger32Value("PropertyDefaultInteger32").toString());}
                    catch (NullPointerException e) {logger.debug("PropertyDefaultInteger32 if property is empty.", e);}
                }
                else if (propertyDefinition.get_DataType() == TypeID.BOOLEAN) {
                    propertyModel.setMaxSize(0);
                    try {propertyModel.setDefaultValue(propertyDefinition.getProperties().getBooleanValue("PropertyDefaultBoolean").toString());}
                    catch (NullPointerException e) {logger.debug("PropertyDefaultBoolean of property is empty.", e);}
                }
                else if (propertyDefinition.get_DataType() == TypeID.DATE) {
                    propertyModel.setMaxSize(0);
                    try {propertyModel.setDefaultValue(propertyDefinition.getProperties().getDateTimeValue("PropertyDefaultDateTime").toString());}
                    catch (NullPointerException e) {logger.debug("PropertyDefaultDateTime of property is empty.", e);}
                }
                else if (propertyDefinition.get_DataType() == TypeID.DOUBLE) {
                    propertyModel.setMaxSize(0);
                    try {propertyModel.setDefaultValue(propertyDefinition.getProperties().getDateTimeValue("PropertyDefaultFloat64").toString());}
                    catch (NullPointerException e) {logger.debug("PropertyDefaultFloat64 of property is empty.", e);}
                }
                else {
                    logger.warn("Bad type property. Type = " + propertyDefinition.get_DataType().toString());
                }
            }
            logger.trace("Returning property model.");
            return propertyModel;  //To change body of implemented methods use File | Settings | File Templates.
        }
        catch (EngineRuntimeException e) {
            logger.error("Error when learning propertyDefinitions", e);
            return null;
        }
    }

    @Override
    public String createPropertyTemplate(P8PropertyModel propertyModel, boolean overwriteIfExist) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isExist(P8PropertyModel propertyModel) {

        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
