package jun.model.commands;

import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.ICmd2ModelAdapter;

public abstract class ACmdReturnDp<R,D,P> extends ADataPacketAlgoCmd<R,D,P>{

	private static final long serialVersionUID = -8598273884857822943L;
	@SuppressWarnings("unused")
	private transient ICmd2ModelAdapter cmd2ModelAdpt;
	
	public ACmdReturnDp(){}
	public ACmdReturnDp(ICmd2ModelAdapter cmd2ModelAdpt){
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}
	
	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2ModelAdpt = cmd2ModelAdpt;
	}
}
