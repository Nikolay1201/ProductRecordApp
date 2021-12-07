package server.exception;

public class NetException extends Exception {
	
	public NetException() {}
	
	public NetException(Throwable e) {
		super(e);
	}
	
	public NetException(String message, Throwable e) {
		super(message, e);
	}
}
