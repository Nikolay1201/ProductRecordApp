package server.dao.db.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import server.dao.db.DBDAO;
import server.dao.db.DataType;
import server.dao.db.Validator;
import server.dao.db.rowoperation.RowOperation;
import server.exception.DBException;
import server.exception.InvalidArgException;
import server.exception.UnsupportedTypeException;
import server.util.bean.Filter;
import server.util.bean.Table;
import server.util.bean.TableInfo;
import server.util.bean.UserRole;
import server.util.constant.MySQLConstants;
import server.util.constant.UserTables;


/*
 * Assume all PK have the only attribute (has length 1)
 */

public class MySQLDAOImpl implements DBDAO {
	
	private Map<String, TableInfo> tablesInfoMap;
	private Connection conn;
	private Statement st;
	
	public MySQLDAOImpl() throws DBException {
		try {
			tablesInfoMap = new HashMap<String, TableInfo>();
			Driver mySqlDriver = new com.mysql.cj.jdbc.Driver();
			DriverManager.registerDriver(mySqlDriver);	
			conn = DriverManager.getConnection(MySQLConstants.DB_URL, MySQLConstants.ADMIN_LOGIN, MySQLConstants.ADMIN_PASSWORD);
			st = conn.createStatement();
			DatabaseMetaData dbMeta = conn.getMetaData();
			TableInfo tableInfo;
			ResultSet tempRS;
			ResultSet tablesRS = dbMeta.getTables(null, null, "%", new String[]{MySQLConstants.TABLE_TYPE});
			String tableName;
			UserTables.map.get(UserRole.SYSADMIN).clear();
			while (tablesRS.next()) {
				if (tablesRS.getString("TABLE_NAME").equals("sys_config")) continue; // CRUTCH!
				tableName = tablesRS.getString("TABLE_NAME");
				//adding all possible tables to allowable tables of sysadmin
				UserTables.map.get(UserRole.SYSADMIN).add(tableName);
				tableInfo = new TableInfo();
				tableInfo.setName(tableName);
				tempRS = dbMeta.getPrimaryKeys(null, null, tableInfo.getName());
				tempRS.next();
				tableInfo.setPkAttrName(tempRS.getString("COLUMN_NAME"));
				tempRS = dbMeta.getColumns(null, null, tableInfo.getName(), null);
				DataType dataType = DataType.getDefault();
				while (tempRS.next()) {
					try {
						dataType = convertDBTypeToDataType(tempRS.getInt("DATA_TYPE"));
					} catch (UnsupportedTypeException e) {
						dataType = DataType.getDefault();
					} finally {
						tableInfo.addAttr(tempRS.getString("COLUMN_NAME"), dataType);
					}
				}
				tablesInfoMap.put(tableInfo.getName(), tableInfo);
			}
		} catch (SQLException e) {
			throw new DBException(e);
		}

	}

	@Override
	public void close() throws DBException {
		try {
			st.close();
			conn.close();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	@Override
	public void executeRowOperation(String tableName, RowOperation rowOperation) throws InvalidArgException, DBException {
		if (tableName == null || rowOperation == null) {
			throw new InvalidArgException();
		}
		if (!Validator.checkOperationToTableValidity(rowOperation, tablesInfoMap.get(tableName))) {
			throw new InvalidArgException("ERROR: The operation " + rowOperation.getType() + 
					" does not correspond to table " + tableName);
		}
		TableInfo tableInfo = tablesInfoMap.get(tableName);
		String pkAttrName = tableInfo.getPkAttrName();
		try {
			String sqlQueryStr = "";
			switch(rowOperation.getType()) {
				case UPDATE_OPERATOIN:
					String argListStr = "";
					for (Map.Entry<String, String> entry : rowOperation.getChangesMap().entrySet()) {
						argListStr = argListStr + "`" + entry.getKey() + "`=\"" + entry.getValue() + "\",";
					}
					argListStr = argListStr.substring(0, argListStr.length() - 1); // CRUTCH!
					sqlQueryStr = "UPDATE `" + tableName + "` SET " + argListStr + 
							" WHERE `" + pkAttrName + "`=\"" + 
							rowOperation.getChangesMap().get(tablesInfoMap.get(tableName).getPkAttrName()) + "\"";
					break;
				case INSERT_OPERATION: 
					String attrNamesListStr = "", attrValListStr = "";
					for (Map.Entry<String, String> entry : rowOperation.getChangesMap().entrySet()) {
						attrNamesListStr = attrNamesListStr + "`" + entry.getKey() + "`,";
						attrValListStr = attrValListStr + "\"" + entry.getValue() + "\",";
					}
					attrNamesListStr = attrNamesListStr.substring(0, attrNamesListStr.length() - 1);
					attrValListStr = attrValListStr.substring(0, attrValListStr.length() - 1);
					sqlQueryStr = "INSERT INTO `" + tableName + "` (" + attrNamesListStr + ") VALUES (" +
							attrValListStr + ")";
					break;
				default:
					break;
				case DELETE_OPERATION:
					sqlQueryStr = "DELETE FROM `" + tableName + "` WHERE `" + 
							pkAttrName + "`=\"" + 
							rowOperation.getChangesMap().get(pkAttrName) + "\"";
					
			}
			System.out.println("execution: " + sqlQueryStr);
			int updRowsCount = st.executeUpdate(sqlQueryStr);
			System.out.println("Affected rows count: " + updRowsCount);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	@Override
	public Map<String, TableInfo> getTablesInfoMap() {
		return tablesInfoMap;
	}

	@Override
	public Table selectFromTableByFilter(String tableName, Filter filter) throws InvalidArgException, DBException {
		if (tableName == null) {
			throw new InvalidArgException();
		} 
		String whereClauseStr;
		try {
			whereClauseStr = filterToWhereClauseStr(filter);
		} catch (InvalidArgException e) {
			whereClauseStr = null;
		}
		String sqlQueryStr = "SELECT * FROM `" + tableName + "` " + 
								((whereClauseStr == null) ? ("") : ("WHERE " + whereClauseStr));
		System.out.println("execution: " + sqlQueryStr);
		try {
			ResultSet res = st.executeQuery(sqlQueryStr);
			TableInfo tableInfo = tablesInfoMap.get(tableName);
			if (tableInfo == null) {
				throw new DBException();
			}
			Table table = new Table(tableInfo);
			ArrayList<String> tempList;
			int rowsCount = 0;
			while (res.next()) {
				tempList = new ArrayList<String>();
				for (int i = 0; i < table.getInfo().getAttrNames().size(); i ++) {
					tempList.add(res.getString(table.getInfo().getAttrNames().get(i)));
				}
				table.addRow(tempList);
				rowsCount ++;
			}
			System.out.println("Selected rows count: " + rowsCount); //Debug
			return table;
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
	
	private String filterToWhereClauseStr(Filter filter) throws InvalidArgException {
		if (filter == null) {
			throw new InvalidArgException();
		}
		if (filter.length() == 0) {
			return null;
		}
		String whereClauseStr = "";
		int i;
		for (i = 0; i < filter.length() - 1; i ++) {
			whereClauseStr = String.format("(`%s` LIKE \"%s\") and", filter.getCondition(i).getAttrName(), 
					filter.getCondition(i).getAttrPattern());
		}
		whereClauseStr = whereClauseStr + String.format(" (`%s` LIKE \"%s\")", filter.getCondition(i).getAttrName(), 
				filter.getCondition(i).getAttrPattern());
		return whereClauseStr;
	}
	
	public static DataType convertDBTypeToDataType(int dbDataType) throws UnsupportedTypeException {
		switch (dbDataType) {
		case (java.sql.Types.DOUBLE):
			return DataType.DOUBLE;
		case (java.sql.Types.LONGVARCHAR):
			return DataType.TEXT;
		case (java.sql.Types.VARCHAR):
			return DataType.TEXT;
		case (java.sql.Types.INTEGER):
			return DataType.INT;
		case (java.sql.Types.DATE):
			return DataType.DATE;
		default:	
			throw new UnsupportedTypeException("Unsupported data type " + dbDataType + " was encountered");
		}
	}
	
	@Deprecated
	public boolean sqlQueryToConsole(String queryStr) {
		try {
			Statement st = conn.createStatement();
			ResultSet res = st.executeQuery(queryStr);
			System.out.println("executed");
			while(res.next()) {
				for (int i = 1; i <= res.getMetaData().getColumnCount(); i ++) {
					System.out.println(res.getString(i));
				}
			}
			res.close();
			st.close();
		} catch (SQLException e) {
			System.out.println("Error executing sql");
		}
		return true;
	}

}
