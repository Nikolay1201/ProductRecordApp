package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import server.exception.InvalidArgException;
import server.exception.ServiceException;
import server.service.ClientHandler;
import server.util.constant.ServerConnectionConstants;

public class Server {

	public static void main(String[] args) {
		System.out.println("Server");
		//crutch for executing static block of UserRole class
		try {
			Class.forName("server.util.constant.UserTables");
		} catch (ClassNotFoundException e1) {
			System.exit(1);
		}
		ServerSocket serverSock = null;
		try {
			try {
				serverSock = new ServerSocket(ServerConnectionConstants.LISTENER_PORT_NUMBER);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			System.out.println(serverSock.toString());
			Socket sock;
			while (true) {
				try {
					sock = serverSock.accept();
					System.out.println("New client with " + sock.toString() + " was connected");
				} catch (IOException e) {
					System.out.println(e.getMessage());
					continue;
				}
				try {
					new Thread(new ClientHandler(sock)).start();
				} catch (InvalidArgException | ServiceException e) {
					e.printStackTrace();
					System.out.print("qwer");
				}
			}
		} finally {
			if (serverSock != null) {
				try {
					serverSock.close();
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}
	
}
