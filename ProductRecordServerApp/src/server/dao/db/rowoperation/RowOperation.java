package server.dao.db.rowoperation;

import java.util.HashMap;
import java.util.Map;

import server.exception.InvalidArgException;

public class RowOperation {
	private RowOperationType type;
	private Map<String, String> attrAndValuesMap;
	
	public RowOperation(RowOperationType operationType) throws InvalidArgException {
		if (operationType == null) {
			throw new InvalidArgException();
		}
		type = operationType;
		attrAndValuesMap = new HashMap<String, String>();
	}
	
	public void addAttrAndValue(String attrName, String attrValue) throws InvalidArgException {
		if (attrName == null || attrValue == null) {
			throw new InvalidArgException();
		}
		attrAndValuesMap.put(attrName, attrValue);
	}
	
	public void clearListOfChanges() {
		attrAndValuesMap.clear();
	}
	
	public Map<String, String> getChangesMap() {
		return attrAndValuesMap;
	}

	public RowOperationType getType() {
		return type;
	}

	public void setType(RowOperationType type) {
		this.type = type;
	}
	 
}
