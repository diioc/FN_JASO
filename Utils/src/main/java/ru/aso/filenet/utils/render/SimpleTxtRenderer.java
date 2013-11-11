package ru.aso.filenet.utils.render;

import org.apache.log4j.Logger;
import ru.aso.filenet.utils.domain.P8ChoiceListModel;
import ru.aso.filenet.utils.domain.P8ClassModel;
import ru.aso.filenet.utils.domain.P8PropertyModel;

import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: asolynin
 * Date: 13.09.13
 * Time: 17:08
 * To change this template use File | Settings | File Templates.
 */
public class SimpleTxtRenderer {
    private final static Logger logger = Logger.getLogger(SimpleTxtRenderer.class);

    public void printModel(P8ClassModel classModel) {
        logger.info("*****************\nМодель класса\n*******************\n*******************");
        logger.info("Display name = " + classModel.getDisplayName());
        logger.info("Symbolic name = " + classModel.getSymbolicName());
        logger.info("Description = " + classModel.getDescription());
        logger.info("Superclass = " + classModel.getParentClassSymbolicName() + "\n");
        logger.info("Свойства класса:\n");
        List<P8PropertyModel> propertyList = classModel.getProperties();
        for (P8PropertyModel propModel : propertyList) {
            logger.info("*********************************************");
            logger.info("Display name = " + propModel.getDisplayName());
            logger.info("Symbolic name = " + propModel.getSymbolicName());
            logger.info("Description = " + propModel.getDescription());
            logger.info("Type = " + propModel.getTypeAsString());
            logger.info("Is required = " + propModel.isRequired());
            logger.info("Is hidden = " + propModel.isHidden());
            if (propModel.getMaxSize() > 0)
                logger.info("Max size = " + propModel.getMaxSize());
            if (propModel.getDefaultValue() != null)
                logger.info("Default value = " + propModel.getDefaultValue());
            if (propModel.hasChoiceList()) {
                P8ChoiceListModel chListModel = propModel.getChoiceListModel();
                logger.info("Choice list name = " + chListModel.getDisplayName());
                logger.info("Choice list type = " + chListModel.getTypeAsInt());
                Properties choiceItems = chListModel.getChoiceItems();
                Set<String> choiceNames = choiceItems.stringPropertyNames();
                logger.info("*****Choices*****");
                for (String choiceName : choiceNames) {
                    logger.info("Choice = [" + choiceName + " - " + choiceItems.getProperty(choiceName) + "]");
                }
            }
        }
    }
}
