package jun.view;

public interface IModelAdapter<HostType> {
	
	public String login(String username);
	
	public void createRoom(String roomname);
	
	public void connect(String address);
	
	public void invite(HostType remoteConn);

	public void logout();	
}
