package client;
	
import client.java.controller.Controller;
import client.java.controller.ControllerFactory;
import client.java.controller.scene.ScenesController;
import client.java.exception.ControllerException;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import server.exception.UnableToConnectException;
import client.java.util.constant.ScenesURL;
import client.java.util.constant.WarnMessages;

public class Client extends Application {
	
	public static Controller controller;
	static {
		try {
			controller = ControllerFactory.getInstance();
		} catch (ControllerException e) {
			ScenesController.showAlert(Alert.AlertType.ERROR, null, WarnMessages.INTERNAL_ERROR, true);
			e.printStackTrace();
			System.exit(1);
		} catch (UnableToConnectException e) {
			ScenesController.showAlert(Alert.AlertType.ERROR, null, WarnMessages.CONNECTION_ERROR, true);
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static void main(String[] args) throws Exception {
		launch(args); 		
	}
	
	@Override
	public void start(Stage primaryStage) {
			ScenesController.setStage(primaryStage);
			ScenesController.switchScene(ScenesURL.LOGIN_SCENE);
	}
	
	@Override 
	public void stop() {
	    controller.close();
	}
}
