package provided.datapacket;

import java.util.Vector;

/**
 * Composite data type for use in data packets: DataPacket&lt;VDataPacket&gt; <br/>
 * Note that Vector&lt;ADataPacket&gt; cannot be used as a data packet data type 
 * directly because type erasure prevents the distinction between 
 * Vector&lt;A&gt; and Vector&lt;B&gt; -- that is, they produce the same Class object.
 * <br/>
 * Usage:<br/>
 * <pre>
 *      // make the vector of data packets
 *      VDataPacket vdp = new VDataPacket();
 *      vdp.add(datapacket1);
 *      vdp.add(datapacket2);
 *      vdp.add(datapacket3);
 *      // etc
 *      // make the composite data packet
 *      ADataPacket vd = new DataPacket&lt;VDataPacket&gt;(VDataPacket.class, vdp);
 * </pre>
 * This class is a vector of the abstract data packets, ADataPacket.  If a vector of more specific 
 * types of data packets is desired, a custom class that is a sub-class of the desired
 * Vector type should be used instead of this class.   Composites made as such would be 
 * distinguishable as per their held types.
 * @author Stephen Wong (c) 2010
 *
 */
public class VDataPacket extends Vector<ADataPacket> {

	private static final long serialVersionUID = -860544422905072718L;

	/**
	 * Convenience method that creates a command that maps a visitor over the vector of data packets.
	 * <br/>
	 * Since the result returned by the returned command is a vector of R, 
	 * the returned command must be wrapped in another command
	 * before it can be used in a recursive algorithm, which would require a return of type R.
	 * @param <R>  The return type of the given visitor.
	 * @param <P>  The vararg input parameter type of the original visitor. 
	 * @param algo The visitor to be mapped over all the stored data packets.
	 * @return An ADataPacketAlgoCmd whose results are a vector of results from applying the given visitor to each data packet element.
	 */
	public static <R, P> ADataPacketAlgoCmd<Vector<R>, VDataPacket, P> makeMapCmd(final DataPacketAlgo<R,P> algo) {
		return new ADataPacketAlgoCmd<Vector<R>, VDataPacket, P>(){

			private static final long serialVersionUID = -5855856243603215928L;

			public Vector<R> apply(Class<?> index, DataPacket<VDataPacket> host, @SuppressWarnings("unchecked") P... params) {
				Vector<R> vResult = new Vector<R>();
				for(ADataPacket d: host.getData()){
					vResult.add(d.execute(algo, params));
				}
				return vResult;
			}
			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
				// not used.
			}
		};
	}
}
