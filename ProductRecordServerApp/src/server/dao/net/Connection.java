package server.dao.net;

import java.net.InetAddress;
import java.net.Socket;

import server.dao.net.message.Message;
import server.exception.InvalidArgException;
import server.exception.NetException;
import server.exception.UnableToConnectException;

public interface Connection {
	void connect(InetAddress addr, int portNumber) throws InvalidArgException, NetException, UnableToConnectException;
	boolean connect(Socket sock) throws InvalidArgException, NetException;
	void close() throws NetException;
	void sendMessage(Message message) throws InvalidArgException, NetException;
	boolean isConnected();
	Message waitMessage() throws UnableToConnectException, NetException;
}
