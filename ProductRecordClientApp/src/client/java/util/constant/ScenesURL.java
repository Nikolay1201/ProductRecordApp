package client.java.util.constant;

import java.net.URL;

import client.Client;

public enum ScenesURL {
	MAIN_SCENE("client/res/scene/Main.fxml"),
	LOGIN_SCENE("client/res/scene/Login.fxml");
	
	private URL sceneURL;

	private ScenesURL(String path) {
		sceneURL = Client.class.getClassLoader().getResource(path);
	}
	
	public URL get() {
		return sceneURL;
	}
}
