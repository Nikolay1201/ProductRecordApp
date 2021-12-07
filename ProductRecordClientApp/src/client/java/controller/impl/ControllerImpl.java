package client.java.controller.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;

import client.java.controller.Controller;
import client.java.controller.scene.MainSceneController;
import client.java.controller.scene.ScenesController;
import client.java.exception.ControllerException;
import client.java.exception.ServiceException;
import client.java.service.Service;
import client.java.util.constant.ScenesURL;
import client.java.util.constant.WarnMessages;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;
import server.dao.db.DataType;
import server.dao.net.message.Message;
import server.exception.InvalidArgException;
import server.exception.NetException;
import server.exception.UnableToConnectException;
import server.util.bean.Filter;
import server.util.bean.Table;
import server.util.bean.TableInfo;
import server.util.bean.UserRole;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class ControllerImpl implements Controller {
	
	private Service service;
	private Map<String, TableInfo> tablesInfoMap;
	
	public ControllerImpl() {}

	@Override
	public void setService(Service service) {
		this.service = service;		
	}
	
	private class LoginThread extends Task<Void> {
		private String login;
		private String password;
		public LoginThread(String login, String password) {
			this.login = login;
			this.password = password;
		}
		@Override
		protected Void call() throws Exception {
			Message m = null;
			try {
				m = service.login(login, password);
			} catch (UnableToConnectException e) {
				onConnectionLost();
				e.printStackTrace();
			} catch (InvalidArgException e) {
				ScenesController.showAlert(Alert.AlertType.ERROR, "Ошибка ввода", WarnMessages.INVALID_ARG, false);
				e.printStackTrace();
			} catch (ServiceException e) {
				ScenesController.showAlert(Alert.AlertType.ERROR, "Внутренняя ошибка", WarnMessages.INTERNAL_ERROR, false);
				e.printStackTrace();
			}
			tablesInfoMap = (Map<String, TableInfo>)m.getParam(1);
			UserRole role = (UserRole)m.getParam(0);
			if (role == null) {
				ScenesController.showAlert(Alert.AlertType.ERROR, "Ошибка авторизации", WarnMessages.LOGIN_FAIED, false);
			}
			System.out.println(role.getName());
			Platform.runLater(new Runnable() {
				private UserRole role;
				@Override
				public void run() {
					MainSceneController mainSceneController = 
							(MainSceneController)ScenesController.switchScene(ScenesURL.MAIN_SCENE);
					mainSceneController.init(role, tablesInfoMap);
				}
				private Runnable init(UserRole role) {
					this.role = role;
					return this;
				}
			}.init((UserRole)m.getParam(0)));
			return null;
		}
		
	}
	@Override
	public void login(String login, String password) {
		Thread th = new Thread(new LoginThread(login, password));
		th.start();
	}
	
	@Override
	public void onConnectionLost() {
		ScenesController.showAlert(Alert.AlertType.ERROR, null, WarnMessages.CONNECTION_LOST, true);	
		System.exit(1);
		//switchScene(ScenesURL.LOGIN_SCENE);
	}
	
	@Override 
	public void sendDebugMessage(String messageStr) {
		try {
			service.sendDebugMessage(messageStr);
		} catch (InvalidArgException | NetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		try {
			service.close();
		} catch (ServiceException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void resetTableView(TableView<ArrayList<String>> tableView, String tableName) {
		if (tableView == null) {
			return;
		}
		tableView.getColumns().clear();
		tableView.getItems().clear();
		if (tablesInfoMap == null) {
			ScenesController.showAlert(Alert.AlertType.ERROR, "Ошибка", 
					"Перед началом работы пройдите авторизацию в системе", false);
			return;
		}
		TableInfo tableInfo = tablesInfoMap.get(tableName);
		if (tableInfo == null) {
			System.out.println("Table " + tableName + " not found");
			return;
		}		
		int columnCount = tableInfo.getAttrNames().size();
		TableColumn<ArrayList<String>, String> column;
		for (int i = 0; i < columnCount; i ++) {
			column = new TableColumn<ArrayList<String>, String>(tableInfo.getAttrNames().get(i));
			/*
			 column.setCellFactory(new Callback<TableColumn<ArrayList<String>,String>, TableCell<ArrayList<String>, String>>() {
				@Override
				public TableCell<ArrayList<String>, String> call(TableColumn<ArrayList<String>, String> col) {
					//return new TextFieldTableCell<ArrayList<String>, String>();
					TableCell<ArrayList<String>, String> cell = new TableCell<ArrayList<String>, String>();
					cell.setGraphic(new TextField(initText));
					return cell;
				}	 
			 });
			column.setCellValueFactory(new Callback<CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {
				private DataType dataType;
				private int index;
				public Callback<CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>> 
						init(DataType dataType, int index) {
					this.dataType = dataType;
					this.index = index;
					return this;
				}
				@Override
			    public ObservableValue<String> call(CellDataFeatures<ArrayList<String>, String> p) {
			        return new SimpleStringProperty(((ArrayList<String>)p.getValue()).get(index));
			    }
			}.init(tableInfo.getAttrTypes().get(i), i));
			*/
			tableView.getColumns().add(column);
		}				
	}

	private class FillTableWithDataThread extends Task<Void> {
		private TableView<ArrayList<String>> tableView;
		private String tableName;
		private Filter filter;
		public FillTableWithDataThread(TableView<ArrayList<String>> tableView, String tableName, Filter filter) {
			this.tableView = tableView;
			this.tableName = tableName;
			this.filter = filter;
		}
		@Override
		protected Void call() throws Exception {
			Table table;
			try {
				table = service.getTableByFilter(tableName, filter);
			} catch (InvalidArgException | NetException e) {
				e.printStackTrace();
				return null;
			}
			Platform.runLater(new Runnable() {
				private TableView<ArrayList<String>> tableView;
				private Table table;
				@Override
				public void run() {
					for (int i = 0; i < table.getData().size(); i ++) {
						tableView.getItems().add(table.getData().get(i));
					}
				}
				private Runnable init(TableView<ArrayList<String>> tableView, Table table) {
					this.table = table;
					this.tableView = tableView;
					return this;
				}
			}.init(tableView,  table));
			return null;
		}
		
	}
	
	@Override
	public void fillTableViewWithData(TableView<ArrayList<String>> tableView, String tableName, Filter filter) {
		
		Thread th = new Thread(new FillTableWithDataThread(tableView, tableName, filter));
		th.start();
		
	}
	
	
}
