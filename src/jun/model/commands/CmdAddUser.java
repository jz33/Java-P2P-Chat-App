package jun.model.commands;

import java.rmi.RemoteException;

import jun.model.IChatRoomViewAdapter;
import jun.model.functions.MyStatusOk;
import provided.datapacket.ADataPacket;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import provided.datapacket.ICmd2ModelAdapter;
import comp310f13.rmiChat.IChatRoom;
import comp310f13.rmiChat.IStatusOk;
import comp310f13.rmiChat.IAddUser;
import comp310f13.rmiChat.IUser;

public class CmdAddUser extends ADataPacketAlgoCmd<ADataPacket, IAddUser, Void>{

	private static final long serialVersionUID = 2523296585262739897L;

	private transient ICmd2ModelAdapter cmd2ModelAdpt;
	private IChatRoom room;
	private IChatRoomViewAdapter view;
	
	public CmdAddUser(){}
	public CmdAddUser(IChatRoom room, IChatRoomViewAdapter view){
		this.room = room;
		this.view = view;
	}
	
	public ADataPacket apply(Class<?> index, DataPacket<IAddUser> host, Void... params) {
		IUser userStub = host.getData().getUser();		
		room.addLocalUser(userStub);
		try {
			view.addUser(userStub.getName());
			view.append(this.getClass().getSimpleName()+" : add user: "+userStub.getName());
		} catch (RemoteException e) {
			System.err.println(this.getClass().getSimpleName()+" : "+e.getMessage());
			e.printStackTrace();
		}
		return new DataPacket<IStatusOk>(IStatusOk.class, cmd2ModelAdpt.getLocalUserStub(), new MyStatusOk());
	}

	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}

}
