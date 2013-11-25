package jun.model.functions;

import provided.datapacket.ADataPacket;
import provided.datapacket.ADataPacketAlgoCmd;
import comp310f13.rmiChat.IAddCmd;

public class MyAddCmd implements IAddCmd{

	private static final long serialVersionUID = -1521849316680395500L;

	private Class<?> id;
	private ADataPacketAlgoCmd<ADataPacket, ?, ?> cmd;
	
	public MyAddCmd(){}
	public MyAddCmd(Class<?> id, ADataPacketAlgoCmd<ADataPacket, ?, ?> newCmd) {
		this.id  = id;
		this.cmd = newCmd;
	}
	
	public Class<?> getID() {
		return this.id;
	}

	public ADataPacketAlgoCmd<ADataPacket, ?, ?> getNewCmd() {
		return this.cmd;
	}

}
