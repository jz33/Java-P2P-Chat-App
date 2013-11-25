package comp310f13.rmiChat;

import java.io.Serializable;

/**
 * Represents a request for the command to process data corresponding to the given ID.
 * @author swong
 *
 */
public interface IRequestCmd extends Serializable {

	/**
	 * The unknown host ID value that was encountered.
	 * @return A host ID.
	 */
	public abstract Class<?> getID();
}
