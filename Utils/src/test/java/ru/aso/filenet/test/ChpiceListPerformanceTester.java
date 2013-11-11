package ru.aso.filenet.test;

import com.filenet.api.admin.ChoiceList;
import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.admin.PropertyDefinition;
import com.filenet.api.constants.FilteredPropertyType;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.TypeID;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;
import org.apache.log4j.Logger;
import ru.aso.filenet.utils.domain.P8ChoiceListModel;
import ru.aso.filenet.utils.implementation.UFactory;

import java.util.Iterator;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: ASolynin
 * Date: 21.03.13
 * Time: 18:53
 * To change this template use File | Settings | File Templates.
 */
public class ChpiceListPerformanceTester {
    private static final String FN_USERNAME = "asolynin";
    private static final String FN_PASSWORD = "XSW@zaq1";
    private static final String FN_SRV_ADDR = "172.28.24.69";
    private static final String FN_SRV_PORT = "9080";
    private static final String FN_WSI_URI = "/wsi/FNCEWS40MTOM/";
    private static final String FN_DOMAIN = "FILENET.TNF";
    private static final String FN_OBJECTSTORE = "SZMN2";
    private static final Logger log = Logger.getLogger(ChpiceListPerformanceTester.class);

    public static void main(String[] args) {
        log.trace("Program start");
        Connection connection = UFactory.connection.getConnection(FN_SRV_ADDR, FN_SRV_PORT, FN_WSI_URI, FN_USERNAME, FN_PASSWORD);
        log.trace("Connection established, UserContext is configurable.");
        Domain domain = Factory.Domain.getInstance(connection, FN_DOMAIN);
        log.trace("Domain retrieved.");
        ObjectStore osSZMN = Factory.ObjectStore.getInstance(domain, FN_OBJECTSTORE);
        log.trace("Object Store SZMN2 retrieved.");

        P8ChoiceListModel listModel = new P8ChoiceListModel();
        listModel.setTypeAsInt(TypeID.STRING_AS_INT);
        listModel.setDisplayName("FastCloseChLs");
        listModel.setDescription("Быстрое закрытие");
        Properties props = new Properties();
        props.setProperty("Неполный", "Неполный");
        props.setProperty("Специальный", "Специальный");
        props.setProperty("Нет", "Нет");
        listModel.setChoiceItems(props);

        long timeBegin = System.currentTimeMillis();
        ChoiceList list = Factory.ChoiceList.getInstance(osSZMN, new Id("{AA7B9D60-8532-4ACE-BC96-7DA3EA137882}"));
        P8ChoiceListModel mode = UFactory.choiceList.getModel(list);
        //UFactory.choiceList.deleteChoiceList(osSZMN, mode);
        UFactory.choiceList.createChoiceList(osSZMN, mode, true);
        log.info("ChoiceList recreated. Elapsed time (ms) = " + (System.currentTimeMillis() - timeBegin));

        /*
        long timeBegin = System.currentTimeMillis();
        PropertyFilter pf = new PropertyFilter();
        pf.addIncludeType(0, null, true, FilteredPropertyType.LIST_OF_OBJECT, null);
        pf.addIncludeProperty(1, null, true, PropertyNames.ID + " " + PropertyNames.CHOICE_LIST + " " + PropertyNames.PROPERTY_TEMPLATE, null);
        SearchScope scope = new SearchScope(osSZMN);
        SearchSQL sql = new SearchSQL("SELECT * FROM ClassDefinition");

        long timeFetchBegin = System.currentTimeMillis();
        Iterator<ClassDefinition> definitionsIter = scope.fetchObjects(sql, 500, pf, true).iterator();
        log.trace("Searching and fetching time ms = " + (System.currentTimeMillis() - timeFetchBegin));

        while (definitionsIter.hasNext()) {
            ClassDefinition classDefinition = definitionsIter.next();
            Iterator<PropertyDefinition> propIter = classDefinition.get_PropertyDefinitions().iterator();
            while (propIter.hasNext()) {
                PropertyDefinition property = propIter.next();
                ChoiceList list = property.get_ChoiceList();
                if (list != null) {
                    log.trace(property.get_PropertyTemplate().get_Id().toString() + "|" + classDefinition.get_Id().toString());
                }
            }
        }
        long timeEnd = System.currentTimeMillis();
        log.info("Try to get ClassDefinitions. Elapsed ms = " + (timeEnd - timeBegin));


        ChoiceList chList = Factory.ChoiceList.getInstance(osSZMN, new Id("{12DC8677-174C-4A4E-81CB-C0941CB3F5FC}"));
        long timeBegin = System.currentTimeMillis();
        P8ChoiceListModel model = UFactory.choiceList.getModel(chList);
        long timeEnd = System.currentTimeMillis();
        log.info("Retrieve choiceList " + model.getDisplayName() + " Elapsed ms = " + (timeEnd - timeBegin));
        */
    }
}
