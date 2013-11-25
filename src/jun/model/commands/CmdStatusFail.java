package jun.model.commands;

import jun.model.IChatRoomViewAdapter;
import jun.model.functions.MyStatusFail;
import provided.datapacket.ADataPacket;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import provided.datapacket.ICmd2ModelAdapter;
import comp310f13.rmiChat.IStatusFail;

public class CmdStatusFail extends ADataPacketAlgoCmd<ADataPacket, IStatusFail, Void>{

	private static final long serialVersionUID = -15907289387495619L;
	private transient ICmd2ModelAdapter cmd2ModelAdpt;
	private IChatRoomViewAdapter view;

	public CmdStatusFail(){}
	public CmdStatusFail(IChatRoomViewAdapter view){
		this.view = view;
	}
	
	public ADataPacket apply(Class<?> index, DataPacket<IStatusFail> host, Void... params) {
		String msg = this.getClass().getSimpleName()+" : fail status received from: "+host.getSender() + " was received: "
				+ host.getData().getMsg();
		view.append(msg);
		return new DataPacket<IStatusFail>(IStatusFail.class,cmd2ModelAdpt.getLocalUserStub(),new MyStatusFail(msg,host));
	}

	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}

}
