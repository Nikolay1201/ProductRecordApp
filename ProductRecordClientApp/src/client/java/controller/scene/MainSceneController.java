package client.java.controller.scene;

import java.util.ArrayList;
import java.util.Map;

import client.Client;
import client.java.exception.ControllerException;
import client.java.util.constant.WarnMessages;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import server.exception.InvalidArgException;
import server.util.bean.TableInfo;
import server.util.bean.UserRole;
	
public class MainSceneController {
	
    @FXML
    private AnchorPane dbDataControlPane;

    @FXML
    private Button deleteButton;

    @FXML
    private ChoiceBox<String> tableChoiseBox;

    @FXML
    private TableView<ArrayList<String>> tableView;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelChangesButton;

    @FXML
    private Button saveChangesButton;

    @FXML
    private Label roleNameLabel;
    
    public void init(UserRole role, Map<String, TableInfo> tablesInfoMap) {
    	switch (role) {
    	case SYSADMIN:
    		break;
    	case ADMIN:
    		break;
    	case EMPLOYEE:
    		break;
    	default:
    		ScenesController.showAlert(Alert.AlertType.ERROR, "Внутренняя ошибка", "Режим не поддержвается", true);
    		System.exit(1);    			
    	}
    	tableChoiseBox.getItems().addAll(tablesInfoMap.keySet());
    	roleNameLabel.setText(String.format("Режим \"%s\"", role.getName()));
		Client.controller.resetTableView(tableView, tablesInfoMap.keySet().iterator().next());
    	tableChoiseBox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String tableName = ((ChoiceBox<String>)event.getSource()).getSelectionModel().getSelectedItem();
				Client.controller.resetTableView(tableView, tableName);
				Client.controller.fillTableViewWithData(tableView, tableName, null);
			}
    	});
    }

}