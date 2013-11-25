package jun.model.commands;

import java.rmi.RemoteException;

import jun.model.IChatRoomViewAdapter;
import jun.model.functions.MyStatusOk;
import provided.datapacket.ADataPacket;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import provided.datapacket.ICmd2ModelAdapter;
import comp310f13.rmiChat.IChatRoom;
import comp310f13.rmiChat.IRemoveUser;
import comp310f13.rmiChat.IStatusOk;
import comp310f13.rmiChat.IUser;

public class CmdRemoveUser extends ADataPacketAlgoCmd<ADataPacket, IRemoveUser, Void>{

	private static final long serialVersionUID = 2860225261443420870L;

	private transient ICmd2ModelAdapter cmd2ModelAdpt;
	private IChatRoom room;
	private IChatRoomViewAdapter view;

	public CmdRemoveUser(){}
	public CmdRemoveUser(IChatRoom room,IChatRoomViewAdapter view){
		this.room = room;
		this.view = view;
	}

	public ADataPacket apply(Class<?> index, DataPacket<IRemoveUser> host, Void... params) {
		IUser userStub = host.getData().getUser();
		room.removeLocalUser(userStub);
		try {
			view.removeUser(userStub.getName());
			view.append(this.getClass().getSimpleName()+" : remove user: "+userStub.getName());
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
