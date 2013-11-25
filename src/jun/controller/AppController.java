package jun.controller;

import java.awt.Component;
import java.util.Collection;

import javax.swing.SwingUtilities;

import provided.util.IVoidLambda;
import comp310f13.rmiChat.IHost;
import jun.model.AppModel;
import jun.model.ChatModel;
import jun.model.IChatRoomViewAdapter;
import jun.model.IViewAdapter;
import jun.view.AppView;
import jun.view.ChatView;
import jun.view.IChatRoomModelAdapter;
import jun.view.IModelAdapter;

public class AppController {

	private AppView<IHost,ChatModel> view;
	private AppModel model;
	
	public static void main(String[] strs){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				(new AppController()).start();
			}
		});
	}
	
	private void start(){
		view.start();
	}
	
	public AppController(){
		this.view = new AppView<IHost,ChatModel>(new IModelAdapter<IHost>(){

			public String login(String username) {
				return model.login(username);
			}

			public void createRoom(String roomname) {
				model.createRoom(roomname);
			}

			public void connect(String address) {
				model.connect(address);
			}

			public void invite(IHost remoteConn) {
				model.invite(remoteConn);
			}

			public void logout() {
				model.logout();
			}
		});
		
		this.model = new AppModel(new IViewAdapter(){

			public IChatRoomViewAdapter getChatRoomViewAdapter(final ChatModel room) {
				final ChatView chatview = view.setChatRoomAdapter(room.getRoom().getName(), new IChatRoomModelAdapter(){

					public void sendMsg(String msg) {
						room.sendMsg(msg);
					}

					public void sendImg(String url) {
						room.sendImg(url);
					}

					public void exitRoom() {
						room.exitRoom();
					}
					
				});
				return new IChatRoomViewAdapter(){

					public void append(String msg) {
						chatview.append(msg);
					}

					public void addUser(String username) {
						chatview.addUser(username);
					}

					public void removeUser(String username) {
						chatview.removeUser(username);
					}

					public void reset() {
						chatview.reset();
					}

					public void exitRoom() {
						view.exitRoom(chatview);
					}
					
				};
			}

			public void append(String s) {
				view.append(s);
			}

			public IVoidLambda<Void> addComponent(String name, Component component) {
				return view.addComponent(name, component);
			}

			public void setConnectedHosts(Iterable<IHost> hosts) {
				view.setConnectedHosts(hosts);
			}

			public ChatModel showRoomSelector(Collection<ChatModel> roomlist) {
				return (ChatModel) view.showRoomSelector(roomlist);
			}

			public boolean acceptInvite(String chatroomInfo) {
				return view.acceptInvite(chatroomInfo);
			}
			
		});
	}
}
