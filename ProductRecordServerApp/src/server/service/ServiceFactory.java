package server.service;

import server.dao.db.DBDAO;
import server.dao.net.Connection;
import server.exception.InvalidArgException;
import server.service.impl.ServiceImpl;

public class ServiceFactory {
	
	private ServiceFactory() {}
	
	public static Service getInstance(Connection connection, DBDAO db) throws InvalidArgException  {
		return new ServiceImpl(connection, db);
	}

}
