package comp310f13.rmiChat;

import java.io.Serializable;

import comp310f13.rmiChat.IUser;


/**
 * Data item that encapsulates the desire to add an IUser stub to the receiver's local chatroom  
 * Implementations should be associated with the host ID 
 * IAddUser.class
 */
public interface IAddUser extends Serializable {

	/**
	 * Get the IUser stub to add.
	 * @return  The IUser stub
	 */
	public IUser getUser();
}
