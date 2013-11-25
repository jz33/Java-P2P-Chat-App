package jun.model.commands;

import jun.model.functions.MyStatusOk;
import provided.datapacket.ADataPacket;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import provided.datapacket.ICmd2ModelAdapter;
import comp310f13.rmiChat.IStatusOk;
import comp310f13.rmiChat.ITextMsg;

/**
 * Send messages
 * @author junzheng
 *
 */
public class CmdTextMsg extends ADataPacketAlgoCmd<ADataPacket, ITextMsg, Void>{

	private static final long serialVersionUID = -4690227393637528302L;
	private transient ICmd2ModelAdapter cmd2ModelAdpt;

	public ADataPacket apply(Class<?> index, DataPacket<ITextMsg> host,Void... params) {
		ITextMsg data = host.getData();
		this.cmd2ModelAdpt.append(data.getName() + "@"+data.getTime()+": " + data.getMsg()+"\n");
		return new DataPacket<IStatusOk>(IStatusOk.class,cmd2ModelAdpt.getLocalUserStub(),new MyStatusOk());
	}

	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}

}
