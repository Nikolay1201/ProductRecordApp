package client.java.controller;

import java.util.ArrayList;

import client.java.service.Service;
import javafx.scene.control.TableView;
import server.util.bean.Filter;


public interface Controller {
	
	void login(String login, String password);
	void setService(Service service);
	void close();
	void onConnectionLost();
	void sendDebugMessage(String messageStr);
	void resetTableView(TableView<ArrayList<String>> tableView, String tableName);
	void fillTableViewWithData(TableView<ArrayList<String>> tableView, String tableName, Filter filter);
}
