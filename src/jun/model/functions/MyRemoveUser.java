package jun.model.functions;

import comp310f13.rmiChat.IRemoveUser;
import comp310f13.rmiChat.IUser;

public class MyRemoveUser implements IRemoveUser {

	private static final long serialVersionUID = -8168158739502077837L;
	private IUser user;
	
	public MyRemoveUser(){}
	public MyRemoveUser(IUser user){
		this.user = user;
	}
	
	public IUser getUser() {
		return this.user;
	}
}
