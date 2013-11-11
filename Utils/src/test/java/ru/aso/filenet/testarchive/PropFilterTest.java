package ru.aso.filenet.testarchive;

import com.filenet.api.admin.ChoiceList;
import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.admin.PropertyDefinition;
import com.filenet.api.admin.PropertyTemplate;
import com.filenet.api.collection.ClassDefinitionSet;
import com.filenet.api.collection.RepositoryRowSet;
import com.filenet.api.constants.FilteredPropertyType;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.core.*;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.RepositoryRow;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;
import org.apache.log4j.Logger;
import ru.aso.filenet.utils.implementation.UFactory;

import java.util.Date;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: ASO
 * Date: 17.10.13
 * Time: 1:18
 * To change this template use File | Settings | File Templates.
 */
public class PropFilterTest {
    private static final String FN_USERNAME = "asolynin";
    private static final String FN_PASSWORD = "XSW@zaq1";
    private static final String FN_SRV_ADDR = "172.28.24.69";
    private static final String FN_SRV_PORT = "9080";
    private static final String FN_WSI_URI = "/wsi/FNCEWS40MTOM/";
    private static final String FN_DOMAIN = "FILENET.TNF";
    private static final String FN_OBJECTSTORE = "SZMN2";
    private static final Logger log = Logger.getLogger(DefinitionParserTester.class);

    public static void main(String[] args) {
        log.trace("Program start");
        Connection connection = UFactory.connection.getConnection(FN_SRV_ADDR, FN_SRV_PORT, FN_WSI_URI, FN_USERNAME, FN_PASSWORD);
        log.trace("Connection established, UserContext is configurable.");
        Domain domain = Factory.Domain.getInstance(connection, FN_DOMAIN);
        log.trace("Domain retrieved.");
        ObjectStore osSZMN = Factory.ObjectStore.getInstance(domain, FN_OBJECTSTORE);
        log.trace("Object Store retrieved.");

        try {
            SearchScope scope = new SearchScope(osSZMN);
            SearchSQL sql = new SearchSQL();
            sql.setQueryString("SELECT * FROM PropertyTemplate WITH INCLUDESUBCLASSES");

            PropertyFilter pf = new PropertyFilter();
            //pf.addIncludeType(2, null, true, FilteredPropertyType.ANY, null);
            pf.addIncludeProperty(1, null, true, PropertyNames.CHOICE_LIST + " " + PropertyNames.DISPLAY_NAME + " " + PropertyNames.PROPERTY_DEFINITIONS
                     , null);
            pf.addIncludeProperty(0, null, false, PropertyNames.IMMEDIATE_SUBCLASS_DEFINITIONS + " " + PropertyNames.DATE_CREATED , null);

            sql.setQueryString("SELECT Id FROM ClassDefinition cd WHERE cd.SymbolicName = 'SZMNDocuments'");
            long beginDate = System.currentTimeMillis();
            Iterator<RepositoryRow> classIdIterator = scope.fetchRows(sql, null, null, false).iterator();
            RepositoryRow idRow = classIdIterator.next();
            String classDefinitionId = idRow.getProperties().getIdValue("Id").toString();
            long endDate = System.currentTimeMillis();
            log.info("Execute 'SELECT id FROM ClassDefinition' statement. Elapsed ms = " + (endDate - beginDate));
            log.info("Class definition id = " + classDefinitionId);

            beginDate = System.currentTimeMillis();
            ClassDefinition classDefinition = Factory.ClassDefinition.getInstance(osSZMN, new Id(classDefinitionId));
            endDate = System.currentTimeMillis();
            log.info("Get class definition. Elapsed ms = " + (endDate - beginDate));

            beginDate = System.currentTimeMillis();
            classDefinition.fetchProperties(pf);
            endDate = System.currentTimeMillis();
            log.info("Class definition refreshed. Elapsed ms = " + (endDate - beginDate));
            log.info(classDefinition.get_DateCreated());
            printChoices(classDefinition);

            Iterator<ClassDefinition> subclassDefinitionIter = classDefinition.get_ImmediateSubclassDefinitions().iterator();
            while (subclassDefinitionIter.hasNext()) {
                ClassDefinition cDefinition = subclassDefinitionIter.next();
                log.info(cDefinition.get_DisplayName());
            }
        }
        catch (Exception e) {
            log.error("ERROR!", e);
        }
    }

    public static void printChoices(ClassDefinition cDef) {
        long beginTime = System.currentTimeMillis();
        Iterator<PropertyDefinition> propDefIter = cDef.get_PropertyDefinitions().iterator();
        while (propDefIter.hasNext()) {
            PropertyDefinition propDef = propDefIter.next();
            ChoiceList chList = propDef.get_ChoiceList();
            if (chList != null)
            log.info("Retrieve ChoiceList = " + chList.get_DisplayName());
        }
        long endTime = System.currentTimeMillis();
        log.info("Print all choice lists of class definition. Elapsed ms = " + (endTime - beginTime));

    }
}
