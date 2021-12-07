package client.java.service.impl;

import java.util.ArrayList;

import client.java.exception.ServiceException;
import client.java.service.Service;
import server.dao.net.Connection;
import server.dao.net.message.Message;
import server.dao.net.message.MessageType;
import server.exception.InvalidArgException;
import server.exception.NetException;
import server.exception.UnableToConnectException;
import server.util.bean.Filter;
import server.util.bean.Table;
import server.util.bean.TableInfo;

public class ServiceImpl implements Service {
	
	private Connection connection;

	@Override
	public Message login(String login, String password) throws InvalidArgException, ServiceException, UnableToConnectException {
		if (login == null || password == null || login == "") {
			throw new InvalidArgException();
		}
		try {
			connection.sendMessage(new Message(MessageType.LOGIN, login, password));
			return connection.waitMessage();
		} catch (InvalidArgException | NetException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override 
	public void close() throws ServiceException {
		try {
			connection.close();
		} catch (NetException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void setConnection(Connection connection) throws ServiceException {
		if (!connection.isConnected()) {
			throw new ServiceException();
		}
		this.connection = connection;
	}

	@Override
	public void sendDebugMessage(String messageStr) throws InvalidArgException, NetException {
		connection.sendMessage(new Message(MessageType.DEBUG, messageStr));
		
	}

	@Override
	public Table getTableByFilter(String tableName, Filter filter) throws InvalidArgException, NetException {
		connection.sendMessage(new Message(MessageType.TABLE_BY_FILTER, tableName, filter));
		Message m = connection.waitMessage();
		return (Table)m.getParam(0);
	}
}
