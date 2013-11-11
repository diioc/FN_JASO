package ru.aso.filenet.utils.helper;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.util.UserContext;
import org.apache.log4j.Logger;

import javax.security.auth.Subject;

/**
 * Created with IntelliJ IDEA.
 * User: ASolynin
 * Date: 25.09.13
 * Time: 19:45
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionProviderImpl implements ConnectionProvider {
    private static final Logger logger = Logger.getLogger(ConnectionProviderImpl.class);
    @Override
    public Connection getConnection(String srvAddress, String port, String wsURI, String userName, String userPassword) {
        logger.trace("getConnection input parameters: srvAddress = " + srvAddress + " port = " + port
        + " wsURI = " + wsURI + " userName = " + userName);

        srvAddress.trim();
        wsURI.trim();
        userName.trim();
        userPassword.trim();
        port.trim();

        if (srvAddress.endsWith("/")) srvAddress = srvAddress.substring(0, srvAddress.length() - 2);
        if (wsURI.startsWith("/")) wsURI = wsURI.substring(1);

        String wsURL = "http://" + srvAddress + ":" + port + "/" +wsURI;
        logger.info("Start connection to URL = " + wsURL);
        try {
            Connection connection = Factory.Connection.getConnection(wsURL);
            Subject subject = UserContext.createSubject(connection, userName, userPassword, "FileNetP8");
            UserContext.get().pushSubject(subject);
            return connection;
        }
        catch (EngineRuntimeException e) {
            logger.error("Error in connection process", e);
            return null;
        }
    }

    @Override
    public Domain getDomain(Connection connection, String domainName) {
        domainName.trim();
        logger.trace("Request for domain = " + domainName);
        Domain domain = Factory.Domain.fetchInstance(connection, domainName, null);
        return domain;
    }

    @Override
    public ObjectStore getObjectStore(Domain domain, String objecStoreName) {
        objecStoreName.trim();
        logger.trace("Request for objectStore = " + objecStoreName);
        ObjectStore objectStore = Factory.ObjectStore.fetchInstance(domain, objecStoreName, null);
        return objectStore;
    }
}
