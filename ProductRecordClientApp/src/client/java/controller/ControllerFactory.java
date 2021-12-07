package client.java.controller;

import client.java.controller.impl.ControllerImpl;
import client.java.exception.ControllerException;
import client.java.exception.ServiceException;
import client.java.service.Service;
import client.java.service.ServiceFactory;
import server.exception.UnableToConnectException;

public class ControllerFactory {
	private static Controller instance = new ControllerImpl();
	
	private ControllerFactory() {}
	
	public static Controller getInstance() throws ControllerException, UnableToConnectException {
		Service service;
		try {
			service = ServiceFactory.getInstance();
		} catch (ServiceException e) {
			throw new ControllerException(e);
		}
		instance.setService(service);
		return instance;
	}
}
