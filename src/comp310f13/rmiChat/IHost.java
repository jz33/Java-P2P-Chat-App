package comp310f13.rmiChat;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

/** 
 * Represents the non-conversation-specific operations the application offers to remote machines.
 * A stub of this class is what the local machine gets from a remote Registry.   A stub of 
 * this class is passed to the remote machine to enable
 * the remote machine to communicate back to the local machine. 
 * A stub of this type will be bound in the Registry with the name IHost.BOUND_NAME 
 * on the port IHost.CONNECTION_PORT 
 * <br><br>
 * Connection and invitation protocol:
 * <ol>
 * <li> Upon receipt of the remote <code>IHost</code> instance from the remote Registry, call <code>sendLocalHostStub(localHostStub)</code> on remote <code>IHost</code> instance to establish 2-way communications.</li>
 * <li> For each desired invitation, call <code>sendInvite(chatroomInfo)</code> on remote <code>IHost</code> instance.</li>
 * <li> If <code>true</code> is returned, call <code>addToChatRoom(localChatRoom)</code> on remote <code>IHost</code> instance.</li>
 * <li> Remote <code>IHost</code> instance is required to send out <code>IAddUser</code> to the supplied chat room as quickly as possible.</li> 
 * </ol>
 */
public interface IHost extends Remote {
	
	/**
	 * Port that IHost stubs use.
	 */
	public static final int CONNECTION_PORT = 2101;
	
	/**
	 * The name to which the IHost object is bound to in the Registry.
	 */
	public static final String BOUND_NAME = "ChatApp.host";
	
	/**
	 * Get a name that is associated with this connection.   Typically,
	 * this would be related to the name the associated IUser returns,
	 * though technically, this is the name of the computer, not 
	 * the user.
	 * @return A name as a string.
	 * @throws RemoteException  Required for RMI transactions.
	 */
	public String getName() throws RemoteException;
	
	
	/**
	 * Returns the unique UUID value associated with all IUsers associated
	 * with this IHost connection, i.e the same value
	 * returned by all this IHost's IUser stubs.
	 * Since value enables different IHost stubs with the same 
	 * name to be differentiated.
	 * @return The unique UUID for this IHost and all its IUsers.
	 * @throws RemoteException  Required for RMI transactions.
	 */	
	public UUID getUUID() throws RemoteException;
	
	/** 
	* Sends the stub of the local IHost to the remote system. 
	* @param localHostStub The stub of the local IHost object 
	* @throws RemoteException  Required for RMI transactions.
	*/ 
	public void sendLocalHostStub(IHost localHostStub) throws RemoteException;
	
	/**
	 * Invite the remote user to join the chat room described by the given information
	 * string, which can contain any information desired, e.g. name of the room, users in the 
	 * room, etc. chatroomInfo should include the inviter's name and the
	 * name of the chatroom.
	 * @param chatroomInfo Description of the chat room to join.
	 * @return true if invitation accepted, false otherwise.
	 * @throws RemoteException  Required for RMI transactions.
	 */
	public boolean sendInvite(String chatroomInfo) throws RemoteException;

	
	/**
	 * Add IUser stub associated with this IHost to the given chat room and thus include the 
	 * given chat room as one in which this IHost is participating. 
	 * Calling this method assumes that the remote user (the person associated with this IHost instance) 
	 * has already accepted an invitation to the given chat room.   
	 * The remote user is required to immediately send out an IAddUser 
	 * datapacket to everyone in the given chatroom to add themselves to the room and minimize
	 * race conditions in which other copies of the chat room have been modified in the interim.
	 * @param localChatRoom  The local chat room to which the remote user has already accepted an invitation. 
	 * @return  true if the joining was successful, false otherwise.
	 * @throws RemoteException  Required for RMI transactions.
	 */
	public boolean addToChatRoom(IChatRoom localChatRoom) throws RemoteException;
	
}
