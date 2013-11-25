package jun.model.functions;

import java.util.Date;

import comp310f13.rmiChat.ITextMsg;

/**
 * 
 * @author junzheng
 *
 */
public class MyTextMsg implements ITextMsg{

	private static final long serialVersionUID = -7882593222439611636L;
	private String sender;
	private Date time;
	private String msg;
	
	public MyTextMsg(){}
	public MyTextMsg(String sender, String msg){
		this.sender = sender;
		this.time = new Date();
		this.msg = msg;
	}
	
	public String getName() {
		return this.sender;
	}

	public Date getTime() {
		return this.time;
	}

	public String getMsg() {
		return this.msg;
	}

}
