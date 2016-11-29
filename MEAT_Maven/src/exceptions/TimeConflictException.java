package exceptions;

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

	public TimeConflictException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
}
