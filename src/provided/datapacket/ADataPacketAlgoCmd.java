package provided.datapacket;

import provided.extvisitor.*;

/**
 * A DataPacketAlgo command that is designed to work on a DataPacket<D> host.
 * <br/>
 * This convenience class both simplifies the command code but also increase type safety by restricting the host type.
 * <br/>
 * Usage:
 * <pre>
 * myDataPacketAlgo.addCmd(MyData.class, new ADataPacketAlgoCmd&lt;MyReturn, MyData, MyParam&gt;(){
 *     private static final long serialVersionUID = aGeneratedUIDvalue;
 *     
 *     public MyReturn apply(DataPacket&lt;MyData&gt; host, MyParam... params){
 *         // your code here
 *     }
 * }
 * </pre>
 * Note:  In Eclipse, the auto-generation of the implemented methods of this class does not work properly.
 * The concrete apply method below is replicated by the automatic method generator because it doesn't 
 * recognize that the method already exists and is final.  Luckily, a compiler error message gets generated
 * in the attempt to override a final method.   Simply delete the extraneous auto-generated method.
 * 
 * @author Stephen Wong (c) 2010
 *
 * @param <R> The return type
 * @param <D> The data type held by the host
 * @param <P> The input parameter type * ----------------------------------------------
 * Restricts command to hosts of type ADataPacket
 */
public abstract class ADataPacketAlgoCmd<R, D, P> implements IExtVisitorCmd<R, Class<?>, P, ADataPacket>{

	private static final long serialVersionUID = -5627902537609466988L;


	/**
	 * The actual method called by the host visitor when the associated case is invoked.   
	 * This method simply forwards the call to the abstract apply method, performing 
	 * an unchecked cast of the host to the required DataPacket type.
	 * @param index  The Class object used to identify the host
	 * @param host The host calling the visitor
	 * @param params Vararg input parameters to be used for processing the host
	 * @return The result of this case.
	 */
	@SuppressWarnings("unchecked")
	final public <T extends IExtVisitorHost<Class<?>, ? super ADataPacket>> R apply(Class<?> index, T host, P... params) {
		return apply(index, (DataPacket<D>) host, params);
	}

	/**
	 * Abstract method that actually performs the processing of the case.   
	 * Here, the host is strongly typed to be the DataPacket type appropriate for the case (D).
	 * @param index The host ID identifying the host
	 * @param host  The DataPacket host calling the visitor
	 * @param params  Vararg input parameter to be used for processing the host
	 * @return  The result of this case.
	 */
	abstract public R apply(Class<?> index, DataPacket<D> host, @SuppressWarnings("unchecked") P...params);

	/**
	 * Sets the ICmd2ModelAdapter for this command to use to communicate with the
	 * local ChatApp host system.   Any implementation that saves this reference 
	 * should mark its internal ICmd2ModelAdapter field as "transient" to prevent it
	 * from being serialized during any transport process.
	 * @param cmd2ModelAdpt  The adapter to the ChatApp model.
	 */
	abstract public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt);
}
