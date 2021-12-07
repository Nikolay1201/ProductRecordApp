package server.service.impl;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;

import server.dao.db.DBDAO;
import server.dao.db.rowoperation.RowOperation;
import server.dao.net.Connection;
import server.dao.net.message.Message;
import server.dao.net.message.MessageType;
import server.exception.DBException;
import server.exception.InvalidArgException;
import server.exception.NetException;
import server.exception.ServiceException;
import server.exception.UnableToConnectException;
import server.exception.UnsupportedTypeException;
import server.service.Service;
import server.util.bean.Filter;
import server.util.bean.Table;
import server.util.bean.TableInfo;
import server.util.bean.UserRole;
import server.util.constant.TableNames;
import server.util.constant.UserTables;

public class ServiceImpl implements Service {  // CRUTCHES HARDCODE
	
	private DBDAO db;
	private Connection connection;
	
	public ServiceImpl(Connection connection, DBDAO db) throws InvalidArgException {
		if (connection == null || db == null) {
			throw new InvalidArgException();
		}
		this.connection = connection;
		this.db = db;
	}
	
	@Override
	public boolean processMessage(Message m) throws InvalidArgException, UnsupportedTypeException, ServiceException {
		if (m == null) {
			throw new InvalidArgException();
		}
		switch (m.getType()) {
			case DISCONNECT:
				close();
				return false;
			case LOGIN:
				login((String)m.getParam(0), (String)m.getParam(1));
				break;
			case TABLE_BY_FILTER:
				sendFilteredTable((String)m.getParam(0), (Filter)m.getParam(1));
				break;
			case UPDATE_TABLE:
				String tableName = (String)m.getParam(0);
				ArrayList<RowOperation> ArrayList = (ArrayList<RowOperation>)m.getParam(1); //what to do?
				for (int i = 0; i < ArrayList.size(); i ++) {
					try {
						db.executeRowOperation(tableName, ArrayList.get(i));
					} catch (DBException | InvalidArgException e) {
						throw new ServiceException(e);
					}
				}
				break;
			case DEBUG:
				System.out.println((String)m.getParam(0));
				break;
			default:
				throw new UnsupportedTypeException();
		}
		return true;
	}
	
	@Override
	public void login(String login, String password) throws InvalidArgException, ServiceException {
		if (login == null || password == null) {
			throw new InvalidArgException();
		}
		try {
			Filter emplyeeFilter = new Filter();
			emplyeeFilter.addCondition("Логин", login);
			emplyeeFilter.addCondition("Пароль", password);
			Table employeeTable; 
			UserRole role;
			Map<String, TableInfo> userTablesInfoMap = null;
			try {
				employeeTable = db.selectFromTableByFilter(TableNames.EMPLOYEES, emplyeeFilter);
				String roleIdStr = employeeTable.getAttr(0, "роли_ID");
				Filter roleFilter = new Filter();
				roleFilter.addCondition("ID", roleIdStr);
				Table roleTable = db.selectFromTableByFilter(TableNames.ROLES, roleFilter);
				if (roleTable.getData().size() != 1) {
					return;
				}
				role = UserRole.byName(roleTable.getAttr(0, "Наименование"));
				ArrayList<String> userAllowableTables = UserTables.map.get(role);
				userTablesInfoMap = new HashMap<String, TableInfo>();
				for (String tableName : db.getTablesInfoMap().keySet()) {
					if (userAllowableTables.contains(tableName)) {
						userTablesInfoMap.put(tableName, db.getTablesInfoMap().get(tableName));
					}
				}
				System.out.println("User " + login + " was authorized as " + role.getName());
			} catch (NoSuchElementException | IndexOutOfBoundsException e) {
				role = null;
			}
			connection.sendMessage(new Message(MessageType.LOGIN, role, userTablesInfoMap));
		} catch (DBException | NetException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void close() throws ServiceException {
		try {
			db.close();
			connection.close();
		} catch (DBException | NetException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Message waitMessage() throws UnableToConnectException, ServiceException {
		try {
			return connection.waitMessage(); 
		} catch (UnableToConnectException e) {
			throw e;
		} catch (NetException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void sendFilteredTable(String tableName, Filter filter) throws ServiceException {
		try {
			connection.sendMessage(new Message(MessageType.TABLE_BY_FILTER, 
					db.selectFromTableByFilter(tableName, filter)));
		} catch (InvalidArgException | NetException | DBException e) {
			throw new ServiceException(e);
		}		
	}	

}
