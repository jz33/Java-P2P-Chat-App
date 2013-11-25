package jun.model;

import java.awt.Component;
import java.util.Collection;
import comp310f13.rmiChat.IHost;
import provided.util.IVoidLambda;

public interface IViewAdapter {

	/*********************************Connect to mini MVC************************************/

	public IChatRoomViewAdapter getChatRoomViewAdapter(ChatModel room);
	
    /*********************************Methods in AppView************************************/
	
	public void append(String s);
	public IVoidLambda<Void> addComponent(String name, Component component);
	public void setConnectedHosts(Iterable<IHost> hosts);
	public ChatModel showRoomSelector(Collection<ChatModel> roomlist);
	public boolean acceptInvite(String chatroomInfo);
	
}
