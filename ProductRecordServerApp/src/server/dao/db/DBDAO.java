package server.dao.db;

import java.util.Map;

import server.dao.db.rowoperation.RowOperation;
import server.exception.DBException;
import server.exception.InvalidArgException;
import server.util.bean.Filter;
import server.util.bean.Table;
import server.util.bean.TableInfo;

public interface DBDAO {
	void close() throws DBException;
	Map<String, TableInfo> getTablesInfoMap();
	void executeRowOperation(String tableName, RowOperation rowOperation) throws DBException, InvalidArgException;
	Table selectFromTableByFilter(String tableName, Filter filter) throws InvalidArgException, DBException;
	@Deprecated
	boolean sqlQueryToConsole(String queryStr);
}
