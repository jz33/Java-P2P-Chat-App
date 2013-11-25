package comp310f13.rmiChat;

import java.io.Serializable;



import comp310f13.rmiChat.IUser;

import provided.datapacket.ADataPacket;


/**
 * Represents a chat room or "conversation", an encapsulation of the IUsers involved in the conversation. 
 * Conceptually, essentially a dispatcher to a collection of of IUser stubs. 
 * Note that instances of this class is NOT Remote objects, that is, they are NOT an RMI server objects! 
 * Entire IChatRoom instances are sent to remote hosts, not stubs.
 */
public interface IChatRoom extends Serializable {
	/**
	 *Returns the name of the chatroom as assigned by its creator. 
	 * @return The name of the chat room
	 */
	public String getName();
	
	/**
	 * Locally add the given user to the chat room.  This 
	 * method does NOT add the user to the remote users' chatrooms.
	 * Assumes that an IAddUser datapacket was already sent
	 * out to all the current users in the chatroom.
	 * @param newUserStub The user stub to add to the chat room
	 */
	public void addLocalUser(IUser newUserStub);
	
	
	/**
	 * Locally remove the given user from the chatroom.  This
	 * method does NOT remove the user from the remote users' chatrooms. 
	 * @param userStub   The user to remove from the chatroom
	 */
	public void removeLocalUser(IUser userStub);
	
	
	/**
	 * Get an iterable collection of user stubs.  
	 * @return An iterable collection of user stubs.
	 */
	public Iterable<IUser> getUsers();
	
	
	/**
	 * Send the given data packet to all the users in the chat room. 
	 * The IUser sender of the data packet is assumed to already be in the data packet.  
	 * This method CANNOT be assumed to be either blocking or non-blocking.
	 * This method cannot be assumed to traverse the users in any particular order, either 
	 * sequentially or in parallel.   
	 * The datapacket is assumed to already contain the IUser stub of the sender.
	 * @param dp  The data packet to send to the users
	 * @return An Iterable of the returned datapackets 
	 */
	public Iterable<ADataPacket> sendMessage(ADataPacket dp);


}
