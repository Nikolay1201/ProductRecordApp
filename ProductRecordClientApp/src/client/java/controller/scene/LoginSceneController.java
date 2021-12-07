package client.java.controller.scene;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.Client;
import client.java.controller.Controller;
import client.java.exception.ControllerException;
import client.java.util.constant.ScenesURL;
import client.java.util.constant.WarnMessages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import server.exception.InvalidArgException;
import server.exception.UnableToConnectException;

public class LoginSceneController {

    @FXML
    private TextField passwordTextField;

    @FXML
    private Text loginText;

    @FXML
    private Button loginButton;

    @FXML
    private TextField loginTextField;
	
	//go to Main scene
	public void onLoginButtonClick(ActionEvent e) throws IOException {
		Client.controller.login(loginTextField.getText(), passwordTextField.getText());
		System.out.printf("Entered login: %s, password: %s\n", loginTextField.getText(), passwordTextField.getText());
		//Client.controller.sendDebugMessage(String.format("Entered login: %s, password: %s", loginTextField.getText(), passwordTextField.getText()));
	}

}
