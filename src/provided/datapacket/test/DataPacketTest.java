package provided.datapacket.test;

import provided.datapacket.*;
import junit.framework.TestCase;
import java.util.Vector;

/**
 * JUnit tests for the datapacket package
 * @author Stephen Wong (c) 2010
 *
 */
public class DataPacketTest extends TestCase {

	
	public void testDataPacket() {
		ADataPacket ds = new DataPacket<String>(String.class, null, "Stuff");
		ADataPacket di = new DataPacket<Integer>(Integer.class, null, 42);
		ADataPacket dd = new DataPacket<Double>(Double.class, null, 3.1415926);

		// make the vector of data packets
		VDataPacket vdp = new VDataPacket();
		vdp.add(ds);
		vdp.add(di);
		vdp.add(dd);
		
		// make the composite data packet
		ADataPacket vd = new DataPacket<VDataPacket>(VDataPacket.class, null, vdp);
		
		System.out.println("class = "+ vd.getClass());
		
		final DataPacketAlgo<String, String> algo = new DataPacketAlgo<String, String>(new ADataPacketAlgoCmd<String, Object, String>(){

			private static final long serialVersionUID = -3838770346677745909L;

			@Override
			public String apply(Class<?> id, DataPacket<Object> host, String... params) {
				Object x = host.getData();
				return "Default case called. data = "+ x;
			}	

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
				// not used.
			}
		});
				
		
		algo.setCmd(String.class, new ADataPacketAlgoCmd<String, String, String>(){

			private static final long serialVersionUID = 7417327345957770087L;

			@Override
			public String apply(Class<?> id, DataPacket<String> host, String... params) {
				String x = host.getData();
				return "String case called. data = "+ x;
			}

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
				// not used.
			}
		});
		
		algo.setCmd(Integer.class, new ADataPacketAlgoCmd<String, Integer, String>(){

			private static final long serialVersionUID = 5948981304362218691L;

			@Override
			public String apply(Class<?> id, DataPacket<Integer> host, String... params) {
				Integer x = host.getData();
				return "Integer case called. data = "+x;
			}

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
				// not used.
			}
		});
		
		
		algo.setCmd(VDataPacket.class, new ADataPacketAlgoCmd<String, VDataPacket, String>(){

			private static final long serialVersionUID = -5626422695894697578L;

			private ADataPacketAlgoCmd<Vector<String>, VDataPacket, String> cmd = VDataPacket.makeMapCmd(algo);
			@Override
			public String apply(Class<?> id, DataPacket<VDataPacket> host, String... params) {
				Vector<String> results = cmd.apply(id, host, params);
				String result = "{\n";
				for(String s: results ){
					result += s + "\n";
				}
				result += "}";
				return "Composite case called. result = "+result;
			}
			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
				// not used.
			}
		});
		
		String s = ds.execute(algo);
		assertEquals("ds.execute(algo)","String case called. data = Stuff", s);
		System.out.println("ds.execute(algo) = "+s);
		s = di.execute(algo);
		assertEquals("di.execute(algo)","Integer case called. data = 42", s);
		System.out.println("di.execute(algo) = "+s);
		s = dd.execute(algo);
		assertEquals("dd.execute(algo)","Default case called. data = 3.1415926", s);
		System.out.println("dd.execute(algo) = "+s);
		s = vd.execute(algo);
		assertEquals("vd.execute(algo)", "Composite case called. result = {\nString case called. data = Stuff\nInteger case called. data = 42\nDefault case called. data = 3.1415926\n}", s);
		System.out.println("vd.execute(algo) = "+s);
		
		
	}

}
