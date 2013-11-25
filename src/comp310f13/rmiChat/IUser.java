package comp310f13.rmiChat;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

import provided.datapacket.ADataPacket;


/**
 * An RMI server object that represents a user in the chat application system.
 * IUser stubs are held by IChatRooms.   IUser stubs are bound
 * to port IUser.CONNECTION_PORT
 */
public interface IUser extends Remote {

	/**
	 * Port that IUser stubs use.
	 */
	public static final int CONNECTION_PORT = 2101;
	
	/**
	 * Accessor for the name of the user
	 * @return The name of the user
	 * @throws RemoteException  Required for RMI transactions.
	 */
	public abstract String getName() throws RemoteException;
	
	/**
	 * Returns the unique UUID identifier for this user.
	 * The identifier is unique for the IHost instance associated
	 * with the IUser. 
	 * Equality of UUID *defines* the equality of IUsers.  
	 * @return The UUID of this IUser
	 * @throws RemoteException  Required for RMI transactions.
	 */
	public abstract UUID getUUID() throws RemoteException;
	
	/**
	 * Sends the data packet to this user from the given user to be processed.
	 * The IUser sender who is sending this message must already be in the data packet.  
	 * @param dp The data packet to send
	 * @return An ADataPacket, most likely a status data type
	 * @throws RemoteException  Required for RMI transactions.
	 */
	public abstract ADataPacket receiveData(ADataPacket dp) throws RemoteException;

	
	
}
