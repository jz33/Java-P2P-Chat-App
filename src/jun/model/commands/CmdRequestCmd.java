package jun.model.commands;

import java.rmi.RemoteException;

import jun.model.IChatRoomViewAdapter;
import jun.model.functions.MyAddCmd;
import jun.model.functions.MyStatusFail;
import jun.model.functions.MyStatusOk;
import provided.datapacket.ADataPacket;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import provided.datapacket.DataPacketAlgo;
import provided.datapacket.ICmd2ModelAdapter;
import comp310f13.rmiChat.IAddCmd;
import comp310f13.rmiChat.IRequestCmd;
import comp310f13.rmiChat.IStatusFail;
import comp310f13.rmiChat.IStatusOk;

public class CmdRequestCmd extends ADataPacketAlgoCmd<ADataPacket, IRequestCmd, Void>{

	private static final long serialVersionUID = -5581840917974616806L;
	private transient ICmd2ModelAdapter cmd2ModelAdpt;
	private DataPacketAlgo<ADataPacket, Void> visitor;
	private IChatRoomViewAdapter view;
	
    public CmdRequestCmd(){}
	public CmdRequestCmd(DataPacketAlgo<ADataPacket, Void> visitor, IChatRoomViewAdapter view) {
		this.visitor = visitor;
		this.view    = view;
	}
	
	public ADataPacket apply(Class<?> index, final DataPacket<IRequestCmd> host, Void... params) {
		try {
			String msg = this.getClass().getSimpleName()+": A Unknown "+ host.getData().getID() + " ID received from "
					+ host.getSender().getName()+ ". New command being sent! \n";
			view.append(msg);
			ADataPacketAlgoCmd<ADataPacket, IAddCmd, Void> cmd = 
					(ADataPacketAlgoCmd<ADataPacket, IAddCmd, Void>) visitor.getCmd(host.getData().getID());
			if(cmd != null)
				return new DataPacket<IAddCmd>(IAddCmd.class,cmd2ModelAdpt.getLocalUserStub(),new MyAddCmd(host.getData().getID(),cmd));
			else{
				msg = this.getClass().getSimpleName()+" : No cmd found for ID: "+host.getData().getID();
				return new DataPacket<IStatusFail>(IStatusFail.class,cmd2ModelAdpt.getLocalUserStub(),new MyStatusFail(msg,host));
			}

		} catch (RemoteException e) {
			System.err.println(this.getClass().getSimpleName()+" : UnknownIDStatus processing: Remote exception: "+e.getMessage());
			e.printStackTrace();
		}
		return new DataPacket<IStatusOk>(IStatusOk.class, cmd2ModelAdpt.getLocalUserStub(), new MyStatusOk());
	}
	
	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}
}
