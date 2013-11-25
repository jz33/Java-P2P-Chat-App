package jun.model.functions;

import provided.datapacket.ADataPacket;
import comp310f13.rmiChat.IStatusFail;

public class MyStatusFail implements IStatusFail{

	private static final long serialVersionUID = -5674541314822039388L;
	private String msg;
	private ADataPacket dp;
	
	public MyStatusFail(){}
	public MyStatusFail(String msg, ADataPacket dp){
		this.msg = msg;
		this.dp  = dp;
	}
	
	public String getMsg() {
		return this.msg;
	}

	public ADataPacket getDataPacket() {
		return this.dp;
	}
}
