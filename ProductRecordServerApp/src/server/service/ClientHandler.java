package server.service;

import java.net.Socket;

import server.dao.db.DBDAOFactory;
import server.dao.net.ConnectionFactory;
import server.dao.net.message.Message;
import server.exception.DBException;
import server.exception.InvalidArgException;
import server.exception.NetException;
import server.exception.ServiceException;
import server.exception.UnableToConnectException;
import server.exception.UnsupportedTypeException;

public class ClientHandler implements Runnable {
	
	private Service service;
	
	public ClientHandler(Socket sock) throws InvalidArgException, ServiceException {
		try {
			service = ServiceFactory.getInstance(ConnectionFactory.getInstance(sock),
												 DBDAOFactory.getInstance());
		} catch (NetException | DBException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void run() {  
		Message m;
		while (true) {
			try {
				m = service.waitMessage();
				if (!service.processMessage(m)) break;
			} catch (InvalidArgException | UnsupportedTypeException e) {
				e.printStackTrace();
			} catch (ServiceException e) {
				e.printStackTrace();
				System.out.println("Fatal error. Client disconnected forcdfully"); 
				return;
			} catch (UnableToConnectException e) {
				System.out.println("Connection with " + e.getUserInfo() + " was unexpectedly lost");
				e.printStackTrace();
				return;
			} 
		}
	}

}
