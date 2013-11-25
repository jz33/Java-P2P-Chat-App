package provided.datapacket;

import comp310f13.rmiChat.IUser;

/**
 * Concrete data packet that holds a generic type of data.
 * 
 * @author Stephen Wong (c) 2010
 *
 * @param <T>  The type of the data being held.  T must be Serializable. * ----------------------------------------------
 * Adds internal data content of type T and host ID type Class<T>
 */
public class DataPacket<T> extends ADataPacket{

	private static final long serialVersionUID = -57375281215880284L;
	
	/**
	 * The data being held
	 */
	private T data;
	
	/**
	 * The constructor for a data packet. <br/>
	 * Usage: <br/>
	 * <pre>
	 * ADataPacket dp = new DataPacket&lt;MyData&gt(MyData.class, aMyData)
	 * </pre>
	 * @param c Must be T.class where T is the data type being used.
	 * @param senderStub  The stub of the IUser sender of the data packet.
	 * @param data  The data to be held in the data packet
	 */
	public DataPacket(Class<T> c, IUser senderStub, T data){
		super(c, senderStub);
		this.data = data;
	}
	
	/**
	 * Accessor for the held data
	 * @return  The data being held
	 */
	public T getData(){
		return data;
	}	
}
