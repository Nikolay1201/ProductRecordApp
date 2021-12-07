package client.java.controller.scene;

import java.io.IOException;
import java.net.URL;

import client.java.controller.Controller;
import client.java.controller.ControllerFactory;
import client.java.util.constant.ScenesURL;
import client.java.util.constant.WarnMessages;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class ScenesController {
	
	private static Stage stage;
	
	public static void setStage(Stage stage) {
		ScenesController.stage = stage;
	}
	
	public static Object switchScene(ScenesURL sceneURL) {
		FXMLLoader loader = new FXMLLoader(sceneURL.get());
		Parent root;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		stage.setScene(new Scene(root));
		stage.show();
		return loader.getController();
	}	
	
	public static void showAlert(Alert.AlertType type, String headerText, String message, boolean needWait) {
		Alert alert = new Alert(type);
		alert.setHeaderText(headerText);
		alert.setContentText(message);
		if (needWait) {
			alert.showAndWait();
		} else {
			alert.show();
		}
	}	
	
}
