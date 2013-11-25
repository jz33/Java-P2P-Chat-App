package jun.model.functions;

import comp310f13.rmiChat.IAddUser;
import comp310f13.rmiChat.IUser;

public class MyAddUser implements IAddUser {

	private static final long serialVersionUID = 3064034186350398309L;
	private IUser user;
	
	public MyAddUser(){}
	public MyAddUser(IUser user){
		this.user = user;
	}
	
	public IUser getUser() {
		return this.user;
	}
}
