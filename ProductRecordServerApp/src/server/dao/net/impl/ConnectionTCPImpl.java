package server.dao.net.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

import server.dao.net.Connection;
import server.dao.net.message.Message;
import server.exception.InvalidArgException;
import server.exception.NetException;
import server.exception.UnableToConnectException;

public class ConnectionTCPImpl implements Connection {
	private Socket sock;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean isConnected;
    
    public void close() throws NetException {
        try {
        	if (in != null) in.close();
			if (out != null) out.close();
	        if (sock != null) sock.close();
		} catch (IOException e) {
			throw new NetException(e);
		}        
    }
    
    @Override
    public Message waitMessage() throws UnableToConnectException, NetException {
    	try {
			/*while (in.available() <= 0) {
				Thread.sleep(WAIT_PERIOD);  // 200ms loose
			}*/
			return (Message)in.readObject();
    	} catch (IOException e) { // CRUTCH!
    		throw new UnableToConnectException(sock.toString(), e);
		} catch (ClassNotFoundException e) { 
			throw new NetException(e);
		}
    }
    
    @Override
    public void sendMessage(Message message) throws InvalidArgException, NetException {
    	if (message == null) {
    		throw new InvalidArgException();
    	}
    	try {
			out.writeObject(message);
			System.out.println("send " + message.getType().toString());
		} catch (IOException e) {
			throw new NetException(e);
		} 
    }
    
    @Override 
    public void finalize() throws NetException {
    	close();
    }

	@Override
	public void connect(InetAddress addr, int portNumber) throws InvalidArgException, NetException, UnableToConnectException {
		if (addr == null) {
			throw new InvalidArgException();
		}
		if (portNumber < 0 || portNumber > 65535) {
			throw new InvalidArgException();
		}
    	try {
			sock = new Socket(addr, portNumber);
			initStreams();
    	} catch (ConnectException e) {
    		throw new UnableToConnectException(String.format("[InetAddr: %s, Port number: %d", addr.toString(), portNumber), e);
    	} catch (IOException e) {
    		isConnected = false;
    		throw new NetException(e);
    	}
    	isConnected = true;
		System.out.println(sock.toString());
	}
	
	@Override
	public boolean connect(Socket sock) throws InvalidArgException, NetException, UnableToConnectException {
		if (sock.isConnected()) {
			isConnected = true;
			this.sock = sock;
			try {
				initStreams();
			} catch (IOException e) {
	    		isConnected = false;
	    		throw new NetException(e);
			}
		} else {
			throw new UnableToConnectException(sock.toString());
		}
		return isConnected;
	}

	@Override
	public boolean isConnected() {
		return isConnected;
	}
	
	private void initStreams() throws IOException {
		out = new ObjectOutputStream(sock.getOutputStream()); 
		in = new ObjectInputStream(sock.getInputStream());
	}
	
}
