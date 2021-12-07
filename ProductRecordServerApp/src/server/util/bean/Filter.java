package server.util.bean;

import java.util.ArrayList;
import java.util.ArrayList;

import server.dao.db.Validator;
import server.exception.InvalidArgException;

public class Filter {
	
	public class FiltrationCondition {
		String attrName;
		String attrPattern;
		
		public FiltrationCondition(String attrName, String attrPattern) {
			this.attrName = attrName;
			this.attrPattern = attrPattern;
		}

		public String getAttrName() {
			return attrName;
		}

		public String getAttrPattern() {
			return attrPattern;
		}
	}
	
	private ArrayList<FiltrationCondition> filterList = new ArrayList<FiltrationCondition>();

	
	public void addCondition(String attrName, String attrPattern) throws InvalidArgException {
		if (!Validator.checkArgSafety(attrPattern) || !Validator.checkArgSafety(attrName)) {
			throw new InvalidArgException("There is a non-safe arg in (" + attrPattern + ", " + attrName + ")");
		}
		if (attrName == null || attrPattern == null) {
			throw new InvalidArgException();
		}
		filterList.add(new FiltrationCondition(attrName, attrPattern));
	}
	
	
	public int length() {
		return filterList.size();
	}
	
	public FiltrationCondition getCondition(int index) {
		return filterList.get(index);
		
	}
}
