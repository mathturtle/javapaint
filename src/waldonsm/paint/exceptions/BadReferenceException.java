package waldonsm.paint.exceptions;

/**
 * A subcategory of NullPointerException which is thrown from my WeakReference subtypes when the backing object has been garbage collected.
 * @author Shawn Waldon
 *
 */
public class BadReferenceException extends NullPointerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new BadReferenceException with the specified message
	 * @param msg
	 */
	public BadReferenceException(String msg) {
		super(msg);
	}

}
