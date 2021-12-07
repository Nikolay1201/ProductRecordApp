package server.util.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import server.dao.db.Validator;
import server.exception.InvalidArgException;

public class Table implements Serializable {
	private static final long serialVersionUID = 8335887647283484226L;
	private TableInfo info;
	private ArrayList<ArrayList<String>> data;
	
	public Table(TableInfo tableInfo) {
		info = tableInfo;
		data = new ArrayList<ArrayList<String>>();
	}
	
	public TableInfo getInfo() {
		return info;
	}

	public void setInfo(TableInfo info) {
		this.info = info;
	}

	public boolean addRow(ArrayList<String> rowValuesList) throws InvalidArgException {
		for (int i = 0; i < rowValuesList.size(); i++) {
			if (!Validator.checkArgSafety(rowValuesList.get(i))) {
				System.out.println("ERROR (Table): There is a non-safe arg " + rowValuesList.get(i));
				return false;
			}
		}
		
		if (rowValuesList.size() != this.info.getAttrNames().size()) {
			System.out.println("ERROR (Table): Attr ArrayList count mismatch");
			return false;
		}
		data.add(rowValuesList);
		return true;
	}
	
	public ArrayList<ArrayList<String>> getData() {
		return data;
	}
	
	public String getAttr(int rowIndex, String name) throws NoSuchElementException { 
		int columnIndex = info.getAttrNames().indexOf(name);
		if (columnIndex == -1) {
			throw new NoSuchElementException();
		}
		return data.get(rowIndex).get(columnIndex);
	}
	
	public boolean isEmpty() {
		return data.size() == 0;
	}
	
	@Deprecated
	public static void toConsole(Table table) {
		System.out.println("Table " + table.info.getName());
		for (int i = 0; i < table.getData().size(); i ++) {
			for (int j = 0; j < table.getData().get(i).size(); j ++) {
				System.out.print(table.getData().get(i).get(j) + ", ");
			}
			System.out.println();
		}
	}
	
}
