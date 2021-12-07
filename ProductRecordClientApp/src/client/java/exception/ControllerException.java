package client.java.exception;

public class ControllerException extends Exception {
	
	public ControllerException(Throwable e) {
		super(e);
	}
	
	public ControllerException(String message) {
		super(message);
	}
	
	public ControllerException(String message, Throwable e) {
		super(message, e);
	}
	
}
