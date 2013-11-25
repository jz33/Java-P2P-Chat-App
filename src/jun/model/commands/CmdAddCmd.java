package jun.model.commands;

import jun.model.IChatRoomViewAdapter;
import jun.model.functions.MyStatusOk;
import provided.datapacket.ADataPacket;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import provided.datapacket.DataPacketAlgo;
import provided.datapacket.ICmd2ModelAdapter;
import comp310f13.rmiChat.IStatusOk;
import comp310f13.rmiChat.IAddCmd;

public class CmdAddCmd extends ADataPacketAlgoCmd<ADataPacket, IAddCmd, Void>{

	private static final long serialVersionUID = -1554606663630896243L;
	private transient ICmd2ModelAdapter cmd2ModelAdpt;
	private DataPacketAlgo<ADataPacket, Void> visitor;
	private IChatRoomViewAdapter view;
	
	public CmdAddCmd(){}
	public CmdAddCmd(DataPacketAlgo<ADataPacket, Void> visitor,IChatRoomViewAdapter view) {
		this.visitor = visitor;
		this.view    = view;
	}
	
	@SuppressWarnings("unchecked")
	public ADataPacket apply(Class<?> index, DataPacket<IAddCmd> host, Void... params) {
		ADataPacketAlgoCmd<ADataPacket, ?,?> cmd =  (ADataPacketAlgoCmd<ADataPacket, ?, ?>) host.getData().getNewCmd();
		if(cmd != null){
			cmd.setCmd2ModelAdpt(cmd2ModelAdpt);
			visitor.setCmd(host.getData().getID(), (ADataPacketAlgoCmd<ADataPacket, ?, Void>) cmd);
			view.append(this.getClass().getSimpleName()+" : add cmd: "+host.getData().getID());
		}
		return new DataPacket<IStatusOk>(IStatusOk.class, cmd2ModelAdpt.getLocalUserStub(), new MyStatusOk());
	}

	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}

}
