package server.dao.net.message;


public enum MessageType {
	
	/* 
	 * to Server: String(login), String(password)
	 * to Client: UserRole, Map<String, TableInfo>
	 */
	LOGIN, 	
	
	/*
	 * to Client: null
	 */
	NO_RIGHTS,
	
	/*
	 * to Server: null
	 * to Client: ArrayList<TableInfo>
	 */
	TABLES_INFO,
	
	/*
	 * to Server: String(table name), Filter
	 * to Client: Table
	 */
	TABLE_BY_FILTER,
	
	/*
	 * to Server: String(table name), ArrayList<RowOperatoin>
	 * to Client: Boolean, String(message)
	 */
	UPDATE_TABLE,
	
	/*
	 * to Server: null
	 */
	DISCONNECT,
	
	/*
	 * to Server: String(message)
	 */
	DEBUG,
}
