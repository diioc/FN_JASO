package ru.aso.filenet.testarchive;

import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.core.*;
import org.apache.log4j.Logger;
import ru.aso.filenet.utils.domain.P8ClassModel;
import ru.aso.filenet.utils.implementation.UFactory;
import ru.aso.filenet.utils.render.ExportFileProvider;
import ru.aso.filenet.utils.render.ModelSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ASolynin
 * Date: 21.03.13
 * Time: 18:53
 * To change this template use File | Settings | File Templates.
 */
public class DefinitionParserTester {
    private static final String FN_USERNAME = "asolynin";
    private static final String FN_PASSWORD = "XSW@zaq1";
    private static final String FN_SRV_ADDR = "172.28.24.69";
    private static final String FN_SRV_PORT = "9080";
    private static final String FN_WSI_URI = "/wsi/FNCEWS40MTOM/";
    private static final String FN_DOMAIN = "FILENET.TNF";
    private static final String FN_OBJECTSTORE = "SZMN2";
    private static final String FILE_OUTPUT_PATH = "D:\\fullSzmnDocModel.p8m";
    private static final Logger log = Logger.getLogger(DefinitionParserTester.class);

    public static void main(String[] args) {
        log.trace("Program start");
        Connection connection = UFactory.connection.getConnection(FN_SRV_ADDR, FN_SRV_PORT, FN_WSI_URI, FN_USERNAME, FN_PASSWORD);
        log.trace("Connection established, UserContext is configurable.");
        Domain domain = Factory.Domain.getInstance(connection, FN_DOMAIN);
        log.trace("Domain retrieved.");
        ObjectStore osSZMN = Factory.ObjectStore.getInstance(domain, FN_OBJECTSTORE);
        log.trace("Object Store SZMN2 retrieved.");


        ClassDefinition szmnDefinition;
        osSZMN.fetchProperties(new String[]{"DatabaseSchemaName"});
        log.trace(osSZMN.get_DatabaseSchemaName());
        szmnDefinition = Factory.ClassDefinition.fetchInstance(osSZMN, "SZMNDocuments", null);
        szmnDefinition.fetchProperties(new String[]{PropertyNames.DISPLAY_NAME});
        log.trace("SZMN Definition retrieved! It's name = " + szmnDefinition.get_DisplayName());

        /*
        List<ClassDefinition> classDefinitions = UFactory.classDefinition.getAllChildren(szmnDefinition);
        SimpleTxtRenderer renderer = new SimpleTxtRenderer();
        renderer.printModel(UFactory.classDefinition.getModel(szmnDefinition, true));
        for (ClassDefinition classDefinition : classDefinitions) {
            renderer.printModel(UFactory.classDefinition.getModel(classDefinition, false));
        }
        */

        List<ClassDefinition> classDefinitions = UFactory.classDefinition.getAllChildren(szmnDefinition);
        List<P8ClassModel> modelList = new ArrayList<P8ClassModel>(classDefinitions.size());

        modelList.add(UFactory.classDefinition.getModel(szmnDefinition, true));
        for (ClassDefinition classDefinition : classDefinitions) {
            modelList.add(UFactory.classDefinition.getModel(classDefinition, false));
        }

        ExportFileProvider renderer = new ModelSerializer();
        renderer.exportToFile(modelList, FILE_OUTPUT_PATH);
    }
}
