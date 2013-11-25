package jun.model;

import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.imageio.ImageIO;

import provided.datapacket.ADataPacket;
import provided.datapacket.DataPacket;
import provided.datapacket.DataPacketAlgo;
import provided.datapacket.ICmd2ModelAdapter;
import provided.util.IVoidLambda;
import jun.model.commands.*;
import jun.model.functions.*;
import comp310f13.rmiChat.*;

public class ChatModel {

	private AppModel model;
	private IUser user;
	private IUser userStub;
	private IChatRoomViewAdapter view;

	private DataPacketAlgo<ADataPacket, Void> dataPacketAlgo;
	private DataPacketAlgo<Boolean, Void> returnPacketAlgo; //Default
	private ICmd2ModelAdapter icmd2modeladapter;
	
	private IChatRoom room = new MyChatRoom("Default Chat Room");
	private ExecutorService execSrvChatRmMgr = Executors.newCachedThreadPool();
	private ArrayList<IVoidLambda<Void>> componentRemovalCmds = new ArrayList<IVoidLambda<Void>>();
	
	/**
	 * Create manager with a given room name
	 * @param roomname
	 * @param model
	 */
	public ChatModel(String roomname, AppModel model){
		this.room = new MyChatRoom(roomname);
		this.model = model;
		this.start();
	}
	
	/**
	 * Constructor
	 * Create manager with a given room
	 * @param room
	 * @param model
	 */
	public ChatModel(IChatRoom room, AppModel model){
		this.room  = room;
		this.model = model;
		this.start();
	}
	
	/**
	 * Prepare
	 * @param username
	 */
	private void start(){
		
		/********************Set view*******************************/
		this.view = model.getChatRoomViewAdapter(this);
		this.view.reset();
		
		/********************Set icmd2modeladapter*******************************/
		this.icmd2modeladapter = new ICmd2ModelAdapter() {

			public IUser getLocalUserStub() {
				return userStub;
			}

			public void append(String s) {
				view.append(s);
			}

			public void addComponent(String name, Component newComp) {
				componentRemovalCmds.add(model.getView().addComponent(name, newComp));
			}
		};
		
		/********************Set returnPacketAlgo*******************************/
		this.returnPacketAlgo = new DataPacketAlgo<Boolean,Void>(new ACmdReturnDp<Boolean,Object,Void>(icmd2modeladapter){

			private static final long serialVersionUID = -2835209930272077850L;

			public Boolean apply(Class<?> index, DataPacket<Object> host, Void... params) {
				try {
					ADataPacket returnDp = host.getSender().receiveData(host.execute(dataPacketAlgo));
					return returnDp.execute(returnPacketAlgo); //Recursively
				} catch (RemoteException e) {
					System.err.println("ChatModel.start./*Set returnPacketAlgo*/ : "+e.getMessage());
					e.printStackTrace();
					return false;
				}
			}
			
		});
		
		this.returnPacketAlgo.setCmd(IStatusOk.class, new ACmdReturnDp<Boolean,IStatusOk,Void>(icmd2modeladapter){

			private static final long serialVersionUID = -5465137932176080994L;

			public Boolean apply(Class<?> index, DataPacket<IStatusOk> host,Void... params) {
				//icmd2modeladapter.append("ChatModel.start./*Set returnPacketAlgo*/ : IStatusOk: "+host.getData());
				return null;
			}
			
		});
		
		this.returnPacketAlgo.setCmd(IStatusFail.class, new ACmdReturnDp<Boolean,IStatusFail,Void>(icmd2modeladapter){

			private static final long serialVersionUID = 5858923225238294484L;

			public Boolean apply(Class<?> index, DataPacket<IStatusFail> host,Void... params) {
				try {
					icmd2modeladapter.append("ChatModel.start./*Set returnPacketAlgo*/: IStatusFail : ("+host.getSender().getName()+") +"+host.getData().getMsg());
				} catch (RemoteException e) {
					System.err.println("ChatModel.start./*Set returnPacketAlgo*/: IStatusFail: error calling IUser.getName(): "+e.getMessage());
					e.printStackTrace();
				}
				return null;
			}
			
		});
		
		/********************Default dataPackegAlgo*******************************/
		this.dataPacketAlgo = new DataPacketAlgo<ADataPacket,Void>(new ACmdReturnDp<ADataPacket,Object,Void>(icmd2modeladapter){

			private static final long serialVersionUID = 2913340114616519024L;

			public ADataPacket apply(Class<?> index, DataPacket<Object> host,Void... params) {
				view.append("ChatModel.start./*Default dataPackegAlgo*/: Unknown host, ID: " + index);
				boolean isReturnedOk = false;
				try {
					ADataPacket returnDP = host.getSender().receiveData(
							new DataPacket<IRequestCmd>(IRequestCmd.class, userStub, new MyRequestCmd(index)));
					isReturnedOk = returnDP.execute(returnPacketAlgo);
					view.append("ChatModel.start./*Default dataPackegAlgo*/: Received cmd from unknown host, ID: " + index);
				} catch (RemoteException e) {
					System.err.println("ChatModel.start./*Default dataPackegAlgo*/:  Error sending unknown packet cmd request: "+e.getMessage());
					e.printStackTrace();
					isReturnedOk = false;
				}
				if(isReturnedOk) return host.execute(dataPacketAlgo);
				else return new DataPacket<IStatusFail>(IStatusFail.class, userStub, new MyStatusFail(""
						+ "ChatModel.start./*Default dataPackegAlgo*/:  Fail to process unknown packet: ", host));
			}
			
		});
		
		/********************Set Cmds to dataPackegAlgo*******************************/
		CmdAddCmd  cmdaddcmd        = new CmdAddCmd(this.dataPacketAlgo,this.view); //
		CmdAddUser    cmdadduser    = new CmdAddUser(this.room, this.view); //
		CmdRemoveUser cmdremoveuser = new CmdRemoveUser(this.room, this.view);//
		CmdRequestCmd cmdrequest    = new CmdRequestCmd(this.dataPacketAlgo,this.view);//
		CmdStatusOk cmdstatusok     = new CmdStatusOk(); //
		CmdStatusFail cmdstatusfail = new CmdStatusFail(this.view);//
		CmdTextMsg cmdtextmsg       = new CmdTextMsg();//
		
		cmdaddcmd.setCmd2ModelAdpt(icmd2modeladapter);
		cmdadduser.setCmd2ModelAdpt(icmd2modeladapter);
		cmdremoveuser.setCmd2ModelAdpt(icmd2modeladapter);
		cmdrequest.setCmd2ModelAdpt(icmd2modeladapter);
		cmdstatusok.setCmd2ModelAdpt(icmd2modeladapter);
		cmdstatusfail.setCmd2ModelAdpt(icmd2modeladapter);
		cmdtextmsg.setCmd2ModelAdpt(icmd2modeladapter);
		
		this.dataPacketAlgo.setCmd(IAddCmd.class, cmdaddcmd);
		this.dataPacketAlgo.setCmd(IAddUser.class, cmdadduser);
		this.dataPacketAlgo.setCmd(IRemoveUser.class, cmdremoveuser);
		this.dataPacketAlgo.setCmd(IRequestCmd.class, cmdrequest);
		this.dataPacketAlgo.setCmd(IStatusOk.class, cmdstatusok);
		this.dataPacketAlgo.setCmd(IStatusFail.class, cmdstatusfail);
		this.dataPacketAlgo.setCmd(ITextMsg.class, cmdtextmsg);
		
		/********************Add default user*******************************/
		this.user = new MyUser(model.getUserName(), dataPacketAlgo, model.getUUID());
		try {
			this.userStub =(IUser) UnicastRemoteObject.exportObject(user, IUser.CONNECTION_PORT);
		} catch (RemoteException e) {
			System.err.println("ChatModel.start./*Add default user*/: error creating user stub: "+e.getMessage());
			e.printStackTrace();
			return;
		}
		
		ADataPacket dp = new DataPacket<IAddUser>(IAddUser.class, userStub, new MyAddUser(userStub));
		
		/**
		 * Send Add user data packet
		 * @author swong
		 */
		this.sendDp(dp,  new IVoidLambdaDP(){

			public void apply(ADataPacket... params) {
				view.append("ChatModel.start./*Add default user*/: Adding self to room: "+ room);
				room.addLocalUser(userStub);
				for (IUser user : room.getUsers())
	 				try {
						view.addUser(user.getName());
					} catch (RemoteException e) {
						System.err.println("ChatModel.start./*Add default user*/:  error creating reading user name: "+e.getMessage());
						e.printStackTrace();
					}
			}
			
		});
	}
	
/**************************************Methods in IChatRoomModelAdapter************************************************/
	public void sendMsg(String msg){
		this.sendDp(new DataPacket<ITextMsg>(ITextMsg.class, this.userStub, new MyTextMsg(model.getUserName(), msg)));
	}
	
	public void sendImg(String url){
		try {
			Image myImg = ImageIO.read(new File(url));
			if(myImg != null) this.sendDp(new DataPacket<IImage>(IImage.class, this.userStub, new MyImage(myImg)));
		} catch (IOException e) {
			System.err.println("ChatModel.sendImg:  error reading image: "+e.getMessage());
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * Exit Room
	 * @author swong
	 */
	public void exitRoom() {
		if(this.userStub != null){
			this.room.removeLocalUser(userStub);
			try {
				view.append("ChatModel.exitRoom: "+userStub.getName()+" is leaving chat room...");
			} catch (RemoteException e) {
				System.err.println("ChatModel.exitRoom: Error getting user name while exiting room: "+ e.getMessage());
				e.printStackTrace();
			}
			
			final BlockingQueue<Boolean> bq = new LinkedBlockingQueue<Boolean>();
			ADataPacket dp = new DataPacket<IRemoveUser>(IRemoveUser.class, userStub, new MyRemoveUser(userStub));
			this.sendDp(dp, new IVoidLambdaDP(){

				public void apply(ADataPacket... params) {
					bq.offer(true);		
				}
				
			});
			
			try {
				bq.take();
				for(IVoidLambda<Void> cmd: componentRemovalCmds){
					cmd.apply();
				}
				view.exitRoom();
				model.exitRoom(this);
			} catch (InterruptedException e) {
				System.err.println("ChatModel.exitRoom: Error retrieving from BlockingQueue: "+ e.getMessage());
				e.printStackTrace();
			}
		}
		
	}

	/**************************************Methods Auxiliary & Getters************************************************/

	/**
	 * Non-blocking sending of a data packet.  
	 * All processing of individual returned data packets may or may not occur asynchronously.
	 * @param data
	 * @author swong
	 */
	public void sendDp(final ADataPacket dp, final IVoidLambdaDP... cmds){
		execSrvChatRmMgr.execute(new Runnable () {
			public void run(){
				Iterable<ADataPacket> resultDPs = room.sendMessage(dp);
				int count = 0;
				for (@SuppressWarnings("unused") ADataPacket dp: resultDPs) count++;
				final CountDownLatch cdLatch = new CountDownLatch(count);
				ExecutorService execSrv = Executors.newCachedThreadPool();
						
				for(ADataPacket dp: resultDPs) {
					final ADataPacket taskDP = dp; 
					execSrv.execute(new Runnable() {
						public void run() {
							if(taskDP!=null) taskDP.execute(returnPacketAlgo); /////
							else view.append("ChatRoomManager.sendDp: Null received as return value!");
							cdLatch.countDown();
							System.out.println("ChatRoomManager.sendDp:  Finshed processing returned data packet: "+ taskDP);
						}
					});	
				}	
				try {
					cdLatch.await();
				} catch (InterruptedException e) {
					view.append("ChatRoomManager.sendDp: InterruptedException e");
					e.printStackTrace();
				}
				
				for(IVoidLambdaDP cmd:cmds) cmd.apply(dp);
			}
		});
	}
	
	public IChatRoom getRoom() {
		return this.room;
	}	
}