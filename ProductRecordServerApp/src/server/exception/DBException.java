package server.exception;

public class DBException extends Exception {
	public DBException(Throwable e) {
		super(e);
	}
	public DBException(String message, Throwable e) {
		super(message, e);
	}
	public DBException() {

	}
}
