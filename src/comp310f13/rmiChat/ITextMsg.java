package comp310f13.rmiChat;

import java.io.Serializable;

/**
 * A data type that encapsulates a text message
 * Implementations should be associated with the host ID
 * ITextMessage.class
 */
public interface ITextMsg extends Serializable {
	/**
	 * The name of the sender
	 * @return The sender's name
	 */
	public String getName();
	
	/**
	 * The time the message was created.
	 * @return  A GMT value
	 */
	public java.util.Date getTime();
	
	/**
	 * The text message being sent.
	 * @return A text string
	 */
	public String getMsg();
}
