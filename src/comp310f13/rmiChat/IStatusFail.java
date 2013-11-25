package comp310f13.rmiChat;

import java.io.Serializable;
import provided.datapacket.ADataPacket;

/**
 * A transmission failure status data type
 * @author swong
 *
 */
public interface IStatusFail extends Serializable {
	
	/**
	 * Get any message from the user
	 * @return A String message from the user 
	 */
	public String getMsg();
	
	/**
	 * Get the failed data packet
	 * @return A datapacket
	 */
	public ADataPacket getDataPacket();
}
