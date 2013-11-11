package ru.aso.filenet.test;

import com.filenet.api.admin.PropertyTemplate;
import com.filenet.api.core.*;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import org.apache.log4j.Logger;
import ru.aso.filenet.testarchive.DefinitionParserTester;
import ru.aso.filenet.utils.implementation.UFactory;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: ASO
 * Date: 17.10.13
 * Time: 1:18
 * To change this template use File | Settings | File Templates.
 */
public class PropTemplateSearchTEster {
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
            pf.addIncludeProperty(new FilterElement(null, null, false, "ChoiceList", null));
            pf.addIncludeProperty(new FilterElement(null, null, false, "SymbolicName", null));

            Iterator<IndependentObject> objectIterator = scope.fetchObjects(sql, 10, pf, true).iterator();
            while (objectIterator.hasNext()) {
                PropertyTemplate propertyTemplate = (PropertyTemplate)objectIterator.next();
                log.info("Prop template: " + propertyTemplate.get_SymbolicName() + " Choice list: "
                        + propertyTemplate.get_ChoiceList());
                if (propertyTemplate.get_ChoiceList() != null) {
                    //log.info("Choice list ID " + propertyTemplate.get_ChoiceList().get_Id());
                    if (propertyTemplate.get_ChoiceList().get_Id().toString().equals("{8873FF1B-8037-4A64-AAF3-E3693B0A21C5}"))
                        log.info("BINGO!");
                }

            }

        }
        catch (Exception e) {
            log.error("ERROR!", e);
        }
    }
}
