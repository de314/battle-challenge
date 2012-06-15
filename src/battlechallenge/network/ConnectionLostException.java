package battlechallenge.network;

public class ConnectionLostException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ConnectionLostException() {
		super();
	}

	public ConnectionLostException(String message) {
		super(message);
	}
}
