package jun.model.functions;

import comp310f13.rmiChat.IRequestCmd;

public class MyRequestCmd implements IRequestCmd{

	private static final long serialVersionUID = -5497835396335790965L;

	private Class<?> id;
	
	public MyRequestCmd(){}
	
	public MyRequestCmd(Class<?> id) {
		this.id = id;
	}
	
	public Class<?> getID() {
	    return this.id;
	}
}
