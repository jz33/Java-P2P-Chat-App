package jun.model.commands;

import jun.model.functions.MyStatusOk;
import provided.datapacket.ADataPacket;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import provided.datapacket.ICmd2ModelAdapter;
import comp310f13.rmiChat.IStatusOk;

public class CmdStatusOk extends ADataPacketAlgoCmd<ADataPacket, IStatusOk, Void> {

	private static final long serialVersionUID = -15077597376754413L;
	private transient ICmd2ModelAdapter cmd2ModelAdpt;
	
	public ADataPacket apply(Class<?> index, DataPacket<IStatusOk> host, Void... params) {
		return new DataPacket<IStatusOk>(IStatusOk.class, cmd2ModelAdpt.getLocalUserStub(), new MyStatusOk());
	}
	
	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}
}
