package jun.model.functions;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.UUID;

import provided.datapacket.ADataPacket;
import comp310f13.rmiChat.IUser;

/**
 * Private class to decorate an IUser to override the equals() and hashCode() 
 * methods so that a dictionary, e.g. Hashtable, can properly compare IUsers.
 * @author swong
 *
 */
public class MyUserProxy implements IUser, Serializable{

	private static final long serialVersionUID = 5351568835844833944L;
	private UUID uuid;
	private IUser userstub;
	
	public MyUserProxy(){}
	public MyUserProxy(IUser userstub) throws RemoteException{
		this.userstub = userstub;
		this.uuid = userstub.getUUID();
	}

	public UUID getUUID() throws RemoteException {
		return this.uuid;
	}
	
	public IUser getStub(){
		return this.userstub;
	}
	
	public String getName() throws RemoteException {
		return this.userstub.getName();
	}

	public ADataPacket receiveData(ADataPacket dp) throws RemoteException {
		return this.userstub.receiveData(dp);
	}
	
	/**
	 * Overridden equals() method to compare UUID's
	 * @return  Equal if UUID's are equal.  False otherwise.
	 */
	@Override
	public boolean equals(Object other){
		if(other instanceof IUser) { // make sure that other object is an IUser
			try {
				return this.userstub.getUUID().equals(((IUser)other).getUUID());
			} catch (RemoteException e) {
				System.err.println("MyUserProxy.equals(): error getting UUID: "+ e);
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * Overridden hashCode() method to create a hashCode from that is hashCode of the UUID since
	 * equality is based on equality of UUID.
	 * @return a hashCode of the UUID.	
	 */
	@Override
	public int hashCode(){
		try {
			// hashCode is shorter than UUID, but Java spec says that if two objects are equal then
			// their hashCodes must also be equal, which will be true here since equals() is based on 
			// UUID equality.  Java does NOT require that unequal entities have unequal hashCodes. 
			return this.userstub.getUUID().hashCode();
		} catch (RemoteException e) {
			System.err.println("MyUserProxy.hashCode(): Error calling remote method on IUser stub: "+e);
			e.printStackTrace();
			return super.hashCode();  // return some sort of hashCode
		}
	}

}
