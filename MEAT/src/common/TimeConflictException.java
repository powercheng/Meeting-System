package common;
/**
 * if meeting date conflicts with another meeting time, attendees' vacation time, and company's holiday,
 * then this exception occurs
 * @author group7
 */
public class TimeConflictException extends Exception {
	/**
	 * This Exception occurs when room availability or attendees time are in conflict 
	 */
	private static final long serialVersionUID = -6565485892997356213L;

	public TimeConflictException() {
		// TODO Auto-generated constructor stub
	}

	public TimeConflictException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public TimeConflictException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public TimeConflictException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public TimeConflictException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}
}