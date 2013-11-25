package jun.model.functions;

import java.rmi.RemoteException;
import java.util.UUID;

import provided.datapacket.ADataPacket;
import provided.datapacket.DataPacketAlgo;
import comp310f13.rmiChat.IUser;

public class MyUser implements IUser {

	private String username;
	private UUID uuid = UUID.randomUUID();
	private DataPacketAlgo<ADataPacket, Void> visitor;
	
	public MyUser(){}
	public MyUser(String username, DataPacketAlgo<ADataPacket, Void> visitor, UUID uuid){
		this.username = username;
		this.visitor  = visitor;
		this.uuid = uuid;
	}
	
	public String getName() throws RemoteException {
		return this.username;
	}

	public UUID getUUID() throws RemoteException {
		return this.uuid;
	}

	public ADataPacket receiveData(ADataPacket dp) throws RemoteException {
		return dp.execute(this.visitor);
	}

}
