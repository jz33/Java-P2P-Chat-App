package comp310f13.rmiChat;

import java.io.Serializable;

import comp310f13.rmiChat.IUser;

/**
 * Data item that represents the desire to remove a user from the receiver's local chatroom.   
 * Implementations should be associated with the host ID
 * IRemoveUser.class
 */
public interface IRemoveUser extends Serializable {

	
	/**
	 * Get the IUser stub to remove from the room
	 * @return The IUser stub to remove
	 */
	public IUser getUser();

}
