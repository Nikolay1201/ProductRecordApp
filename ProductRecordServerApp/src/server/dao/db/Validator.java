package server.dao.db;

import java.util.Map;

import server.dao.db.rowoperation.RowOperation;
import server.exception.InvalidArgException;
import server.util.bean.Filter;
import server.util.bean.TableInfo;
import server.util.constant.MySQLConstants;

public final class Validator {
	public static boolean checkOperationToTableValidity(RowOperation rowOperation, TableInfo tableInfo) throws InvalidArgException {
		if (rowOperation == null || tableInfo == null) {
			throw new InvalidArgException();
		}
		for (Map.Entry<String, String> entry : rowOperation.getChangesMap().entrySet()) {
			if (!tableInfo.getAttrNames().contains(entry.getKey())) {
				return false;
			}
		}
		return true;
	}
	
	//finish up!
	public static boolean checkFilterToTableValididty(Filter filter, TableInfo tableInfo) {
		return true;
	}
	
	// CRUTCH
	public static boolean checkArgSafety(String arg) throws InvalidArgException {
		if (arg == null) {
			throw new InvalidArgException();
		}
		return !(arg.contains("\"") || arg.contains("`"));
	}
	
}
