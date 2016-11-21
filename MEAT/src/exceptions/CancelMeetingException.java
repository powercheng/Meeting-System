package exceptions;

public class CancelMeetingException extends Exception {
	private static final long serialVersionUID = -6565485892997356213L;

	public CancelMeetingException(String msg) {
		super(msg);
	}
}
