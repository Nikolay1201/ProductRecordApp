package server.util.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.ArrayList;

import server.dao.db.DataType;

public class TableInfo implements Serializable {
	private static final long serialVersionUID = -6186621316982739998L;
	private String name;
	private String pkAttrName;
	private ArrayList<String> attrNameList;
	private ArrayList<DataType> attrTypeList;
	
	public TableInfo() {
		attrNameList = new ArrayList<String>();
		attrTypeList = new ArrayList<DataType>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPkAttrName() {
		return pkAttrName;
	}

	public void setPkAttrName(String pkAttrName) {
		this.pkAttrName = pkAttrName;
	}

	public ArrayList<String> getAttrNames() {
		return attrNameList;
	}

	public ArrayList<DataType> getAttrTypes() {
		return attrTypeList;
	}
	
	public void addAttr(String attrName, DataType attrType) {
		attrNameList.add(attrName);
		attrTypeList.add(attrType);
	}
	
	
}
