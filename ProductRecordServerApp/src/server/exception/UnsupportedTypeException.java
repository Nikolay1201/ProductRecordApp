package server.exception;

public class UnsupportedTypeException extends Exception {
	
	public UnsupportedTypeException() {

	}
	
	public UnsupportedTypeException(String errorMessage) {
		super(errorMessage);
	}
}
