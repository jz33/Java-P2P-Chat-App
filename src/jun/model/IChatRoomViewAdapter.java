package jun.model;

public interface IChatRoomViewAdapter {
	
	/***************** Methods in ChatView ********************/
	public void append(String msg);
	public void addUser(String username);
	public void removeUser(String username);
	public void reset();
	
	/***************** Methods in AppView ********************/
	public void exitRoom();

}
