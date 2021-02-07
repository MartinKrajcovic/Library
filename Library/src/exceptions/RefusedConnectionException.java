package exceptions;

@SuppressWarnings("serial")
public class RefusedConnectionException extends Exception {
	// komentar
	public RefusedConnectionException() {
		super();;
	}
	
	public RefusedConnectionException(String message) {
		super(message);
	}

	public RefusedConnectionException(Throwable cause, String message) {
		super(cause);
		
	}
	
}
