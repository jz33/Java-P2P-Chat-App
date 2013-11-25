package jun.model;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.UUID;

import jun.model.functions.MyHost;
import comp310f13.rmiChat.IChatRoom;
import comp310f13.rmiChat.IHost;
import comp310f13.rmiChat.IUser;
import provided.rmiUtils.IRMIUtils;
import provided.rmiUtils.IRMI_Defs;
import provided.rmiUtils.RMIUtils;
import provided.util.IVoidLambda;

public class AppModel {

	private Registry registry;
	private IHost remoteConn;
	private IHost localHostStub;
	private UUID uuid = UUID.randomUUID();
	private String username = "Jun's Host";
	private IViewAdapter view;
	
	/**
	 * Connected remote hosts
	 */
	private ArrayList<IHost> hostlist = new ArrayList<IHost>();
	
	/**
	 * Chat rooms
	 */
	private ArrayList<ChatModel> roomlist = new ArrayList<ChatModel>();
	
	public AppModel(IViewAdapter view){
		this.view = view;
	}
	
	private IRMIUtils rmiUtils = new RMIUtils(new IVoidLambda<String>() {

		public void apply(String... params) {
			for (String s : params) {
				view.append("AppModel: " + s);
			}
		}

	});

	private IVoidLambda<String> outputCmd = new IVoidLambda<String>() {
		public void apply(String... strs) {
			for (String s : strs) {
				view.append("AppModel: " + s);
			}
		}
	};
	
/**************************************Methods in IModelAdapter************************************************/
	public String login(final String username){
		this.username = username;
		rmiUtils.startRMI(IRMI_Defs.CLASS_SERVER_PORT_SERVER);
		IHost conn = new IHost() {

			public void sendLocalHostStub(IHost localHostStub)
					throws RemoteException {
				view.append("AppModel.startRMI().IHost.sendLocalHostStub(): received localHostStub = "+localHostStub);
				IHost localHostStubProxy = new MyHost(localHostStub);
				hostlist.add(localHostStubProxy);
				view.append("Connected to "+localHostStubProxy+"\n");
				view.setConnectedHosts(hostlist);
			}

			public boolean sendInvite(String chatroomInfo)
					throws RemoteException {
				view.append("AppModel.startRMI().sendInvite(): received chatroomInfo = "+chatroomInfo);
				return view.acceptInvite(chatroomInfo);
			}

			public boolean addToChatRoom(IChatRoom localChatRoom)
					throws RemoteException {
				view.append("Attempting to add chat room: " +localChatRoom);
				if(!ifInRoomList(localChatRoom)){
					roomlist.add(new ChatModel(localChatRoom,AppModel.this));
					view.append("Succesfully added "+localChatRoom.getName());
					return true;
				}
				else{
					view.append("User "+username+" is already in "+localChatRoom.getName()+".  Adding this chat room is thus rejected");
					return false;
				}
			}

			public String getName() throws RemoteException {
				return username;//
			}

			public UUID getUUID() throws RemoteException {
				return uuid;
			}

		};
		
		registry = rmiUtils.getLocalRegistry();
		
		try {
			localHostStub = (IHost) UnicastRemoteObject.exportObject(conn, IHost.CONNECTION_PORT);
			registry.rebind(IHost.BOUND_NAME, localHostStub);
		} catch (Exception e) {
			view.append("AppModel.login: Create localhost failed!");
			e.printStackTrace();
		}
		
		outputCmd.apply("Waiting...");
		
		return System.getProperty("java.rmi.server.hostname");
	}
	
	public void createRoom(String roomname){
		this.roomlist.add(new ChatModel(roomname, this));
	}
	
	public void connect(String address){
		Registry remoteRegistry = rmiUtils.getRemoteRegistry(address);
		try {
			remoteConn = new MyHost((IHost)remoteRegistry.lookup(IHost.BOUND_NAME));
			hostlist.add(remoteConn);
			
			view.append("Connected to "+remoteConn+"\n");
			view.setConnectedHosts(this.hostlist);
			
			remoteConn.sendLocalHostStub(localHostStub);
			this.invite(remoteConn);
			
		} catch (AccessException e) {
			view.append("AppModel.connect: AccessException e");
			e.printStackTrace();
		} catch (RemoteException e) {
			view.append("AppModel.connect: RemoteException e");
			e.printStackTrace();
		} catch (NotBoundException e) {
			view.append("AppModel.connect: NotBoundException e");
			e.printStackTrace();
		}
	}
	
	public void invite(final IHost remoteConn){
		view.append("AppModel.invite: "+remoteConn);
		final ChatModel selectedRoom = view.showRoomSelector(roomlist);
		if(selectedRoom == null) return;
		
		// don't block the GUI thread on the remote call --swong
		(new Thread(){
			public void run(){
				String chatroomInfo = username+"'s "+selectedRoom.getRoom().getName()+" chat room";
				try {
					if(remoteConn.sendInvite(chatroomInfo)){
						view.append("Invitation to \""+chatroomInfo+"\" was accepted.\n");
						remoteConn.addToChatRoom(selectedRoom.getRoom());
					}
					else{
						view.append("Invitation to \""+chatroomInfo+"\" was rejected.\n");
					}
				} catch (RemoteException e) {
					view.append("AppModel.invite: RemoteException e: "+remoteConn);
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void logout(){
		for(ChatModel room: roomlist.toArray(new ChatModel[roomlist.size()])) {
			room.exitRoom();
		}
		
		if (registry != null)
			try {
				registry.unbind(IHost.BOUND_NAME);
				rmiUtils.stopRMI();
			} catch (AccessException e) {
				view.append("AppModel.logout: AccessException e");
				e.printStackTrace();
			} catch (RemoteException e) {
				view.append("AppModel.logout: RemoteException e");
				e.printStackTrace();
			} catch (NotBoundException e) {
				view.append("AppModel.logout: NotBoundException e");
				e.printStackTrace();
			}
	}

	/**************************************Methods Auxiliary, Getters************************************************/

	/**
	 * Check if the local user is in the chat room already
	 * @param room  the chat room to check
	 * @return  true if NOT in the room, false otherwise.
	 */
	private boolean ifInRoomList(IChatRoom room) {
		for(IUser user: room.getUsers()) {
			try {
				if(uuid.equals(user.getUUID())) return true;
			} catch (RemoteException e) {
				view.append("ChatModel.ifInRoomList: Error reading user's UUID: "+ e.getMessage());
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public void exitRoom(ChatModel room){
		this.roomlist.remove(room);
	}
	
	public IViewAdapter getView() {
		return this.view;
	}
	
	public String getUserName() {
		return this.username;
	}
	
	public UUID getUUID(){
		return this.uuid;
	}
	
	public IChatRoomViewAdapter getChatRoomViewAdapter(ChatModel chatRoomModel) {
		return view.getChatRoomViewAdapter(chatRoomModel);
	}
}