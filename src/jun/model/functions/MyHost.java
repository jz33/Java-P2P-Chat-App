package jun.model.functions;

import java.rmi.RemoteException;
import java.util.UUID;
import comp310f13.rmiChat.IChatRoom;
import comp310f13.rmiChat.IHost;

public class MyHost implements IHost{

	private String hostname;
	private IHost hostStub;
	
	public MyHost(){}
	public MyHost(IHost hostStub) throws RemoteException{
		this.hostStub = hostStub;
		this.hostname = hostStub.getName();
	}
	
	public String getName() throws RemoteException {
		return this.hostStub.getName();
	}

	public UUID getUUID() throws RemoteException {
		return this.hostStub.getUUID();
	}

	public void sendLocalHostStub(IHost localHostStub) throws RemoteException {
		this.hostStub.sendLocalHostStub(localHostStub);
	}

	public boolean sendInvite(String chatroomInfo) throws RemoteException {
		return this.hostStub.sendInvite(chatroomInfo);
	}

	public boolean addToChatRoom(IChatRoom localChatRoom)
			throws RemoteException {
		return this.hostStub.addToChatRoom(localChatRoom);
	}
	
	/**
	 * use the cached value to minimize network traffic and avoid the RemoteException.
	 * @author swong
	 */
	public String toString(){
		return this.hostname;
	}

}
