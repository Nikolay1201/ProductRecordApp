package server.dao.net.message;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import server.exception.InvalidArgException;


public class Message implements Serializable {
	private static final long serialVersionUID = -5691388829248086918L;
	private MessageType type;
	private ArrayList<Object> paramList = new ArrayList<Object>();
	
	public Message () {};
	
	public Message(MessageType type, Object... objects) throws InvalidArgException {
		if (type == null || objects.length == 0) {
			throw new InvalidArgException();
		}
		this.type = type;
		add(objects);
	}
	
	public MessageType getType() {
		return type;
	}
	
	public void setType(MessageType type) throws InvalidArgException {
		if (type == null) {
			throw new InvalidArgException();
		}
		this.type = type;
	}
	
	public Object getParam(int index) {
		return paramList.get(index);
	}
	
	public void add(Object obj) throws InvalidArgException {
		if (obj == null) {
			throw new InvalidArgException();
		}
		paramList.add(obj);
	}
	
	public void add(Object... objects) throws InvalidArgException {
		if (objects.length == 0) {
			throw new InvalidArgException();
		}
		paramList.addAll(Arrays.asList(objects));
	}
}
