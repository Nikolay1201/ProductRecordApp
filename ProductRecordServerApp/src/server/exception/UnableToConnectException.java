package server.exception;

public class UnableToConnectException extends NetException {
	
	private String userInfo;
	
	public UnableToConnectException(String userInfo, Throwable e) {
		super(e);
		this.userInfo = userInfo;
	}
	
	public UnableToConnectException(String userInfo) {
		this.userInfo = userInfo;
	}

	public String getUserInfo() {
		return userInfo;
	}
}
