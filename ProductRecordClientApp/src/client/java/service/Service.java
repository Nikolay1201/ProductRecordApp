package client.java.service;

import client.java.exception.ServiceException;
import server.dao.net.Connection;
import server.dao.net.message.Message;
import server.exception.InvalidArgException;
import server.exception.NetException;
import server.exception.UnableToConnectException;
import server.util.bean.Filter;
import server.util.bean.Table;

public interface Service {
	void setConnection(Connection connection) throws ServiceException;
	Message login(String login, String password) throws InvalidArgException, ServiceException, UnableToConnectException;
	Table getTableByFilter(String tableName, Filter filter) throws InvalidArgException, NetException;
	void close() throws ServiceException;
	void sendDebugMessage(String messageStr) throws InvalidArgException, NetException;
}
