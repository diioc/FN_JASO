package ru.aso.filenet.utils.helper;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.ObjectStore;

/**
 * Created with IntelliJ IDEA.
 * User: ASolynin
 * Date: 25.09.13
 * Time: 19:35
 * To change this template use File | Settings | File Templates.
 */
public interface ConnectionProvider {
    Connection getConnection(String srvAddress, String port, String wsURI, String userName, String userPassword);
    Domain getDomain(Connection connection, String domainName);
    ObjectStore getObjectStore (Domain domain, String objectStoreName);
}
