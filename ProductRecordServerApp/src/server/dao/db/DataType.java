package server.dao.db;

public enum DataType {
	TEXT,
	INT,
	DOUBLE,
	DATE;
	public static DataType getDefault() {
		return TEXT;
	}
}
