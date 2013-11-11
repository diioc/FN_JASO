package ru.aso.filenet.test;

import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.constants.ChoiceType;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.TypeID;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import org.apache.log4j.Logger;
import ru.aso.filenet.utils.domain.P8ChoiceListModel;
import ru.aso.filenet.utils.domain.P8ClassModel;
import ru.aso.filenet.utils.implementation.UFactory;
import ru.aso.filenet.utils.render.ExportFileProvider;
import ru.aso.filenet.utils.render.ModelSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: ASolynin
 * Date: 21.03.13
 * Time: 18:53
 * To change this template use File | Settings | File Templates.
 */
public class ChpiceListUtilsTester {
    private static final String FN_USERNAME = "asolynin";
    private static final String FN_PASSWORD = "XSW@zaq1";
    private static final String FN_SRV_ADDR = "172.28.24.69";
    private static final String FN_SRV_PORT = "9080";
    private static final String FN_WSI_URI = "/wsi/FNCEWS40MTOM/";
    private static final String FN_DOMAIN = "FILENET.TNF";
    private static final String FN_OBJECTSTORE = "SZMN2";
    private static final Logger log = Logger.getLogger(ChpiceListUtilsTester.class);

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
        Properties props = new Properties();
        props.setProperty("Неполный", "Неполный");
        props.setProperty("Специальный", "Специальный");
        props.setProperty("Нет", "Нет");
        listModel.setId("{8873FF1B-8037-4A64-AAF3-E3693B0A21C5}");
        listModel.setChoiceItems(props);

        UFactory.choiceList.createChoiceList(osSZMN, listModel, true);

    }
}
