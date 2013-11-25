package comp310f13.rmiChat;

import java.io.Serializable;
import provided.datapacket.*;

/**
 * Represents the data type containing information to add 
 * a new command to the data packet processing visitor.   
 * Implementations should be associated with the host ID 
 * IAddCommand.class
 */
public interface IAddCmd extends Serializable {
	
	/**
	 * The host ID to associate with the new command
	 * @return A host ID value
	 */
	public Class<?> getID();
	
	/**
	 * The command to install into the data packet processing visitor
	 * that is associated with the supplied host ID.
	 * @return A data packet visitor command.
	 */
	public  ADataPacketAlgoCmd<ADataPacket, ?, ?> getNewCmd();

}
