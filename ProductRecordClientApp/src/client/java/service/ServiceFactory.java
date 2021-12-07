package client.java.service;

import client.java.exception.ServiceException;
import client.java.service.impl.ServiceImpl;
import server.dao.net.Connection;
import server.dao.net.ConnectionFactory;
import server.exception.InvalidArgException;
import server.exception.NetException;
import server.exception.UnableToConnectException;
import server.util.constant.ServerConnectionConstants;

public class ServiceFactory {
	
	private static Service instance = new ServiceImpl();
	
	private ServiceFactory() {}
	
	public static Service getInstance() throws ServiceException, UnableToConnectException {
		Connection connection;
		try {
			connection = ConnectionFactory.getInstance(
					ServerConnectionConstants.SERVER_INET_ADDR, 
					ServerConnectionConstants.LISTENER_PORT_NUMBER);
		} catch (UnableToConnectException e) {
			throw e;
		} catch (InvalidArgException | NetException e) {
			throw new ServiceException(e);
		}
		instance.setConnection(connection);
		return instance;
	}
}
