package server.dao.net;

import java.net.InetAddress;
import java.net.Socket;

import server.dao.net.impl.ConnectionTCPImpl;
import server.exception.InvalidArgException;
import server.exception.NetException;
import server.exception.UnableToConnectException;

public class ConnectionFactory {

	private ConnectionFactory() {}
	
	public static Connection getInstance(InetAddress addr, int portNumber) 
			throws InvalidArgException, NetException, UnableToConnectException {
		Connection connection = new ConnectionTCPImpl();
		connection.connect(addr, portNumber);
		return connection;
	}
	
	public static Connection getInstance(Socket sock) throws InvalidArgException, NetException, UnableToConnectException {
		Connection connection = new ConnectionTCPImpl();
		connection.connect(sock);
		return connection;
	}
}
