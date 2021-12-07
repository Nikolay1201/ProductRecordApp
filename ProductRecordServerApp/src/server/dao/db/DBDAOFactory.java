package server.dao.db;

import server.dao.db.impl.MySQLDAOImpl;
import server.exception.DBException;

public class DBDAOFactory {
	
	private DBDAOFactory() {}
	
	public static DBDAO getInstance() throws DBException {
		return new MySQLDAOImpl();
	}
}
