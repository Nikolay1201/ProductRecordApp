package server.service;

import server.dao.net.message.Message;
import server.exception.InvalidArgException;
import server.exception.ServiceException;
import server.exception.UnableToConnectException;
import server.exception.UnsupportedTypeException;
import server.util.bean.Filter;

public interface Service {
	Message waitMessage() throws ServiceException, UnableToConnectException;
	boolean processMessage(Message m) throws InvalidArgException, UnsupportedTypeException, ServiceException;
	void login(String login, String password) throws InvalidArgException, ServiceException;
	void sendFilteredTable(String tableName, Filter filter) throws ServiceException;
	void close() throws ServiceException;
}
