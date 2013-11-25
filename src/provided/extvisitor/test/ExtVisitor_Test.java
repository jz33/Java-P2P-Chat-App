package provided.extvisitor.test;

import provided.extvisitor.*;
import junit.framework.TestCase;


public class ExtVisitor_Test extends TestCase {

	/**
	 * Tests on hosts that are different instances of the same class.
	 * 
	 */
	public void test_HostStr() {
		HostStr hostA = new HostStr("a");

		HostStrVis  algo = new HostStrVis(); 

		algo.setCmd("a", new IExtVisitorCmd<Integer, String, Integer, HostStr>() {

			private static final long serialVersionUID = 3285093099208748428L;

			@Override
			public <T extends IExtVisitorHost<String, ? super HostStr>> Integer apply(
					String index, T host, Integer... params) {
				return 42;
			}
		});

		algo.setCmd("q", new IExtVisitorCmd<Integer, String, Integer, HostStr>() {

			private static final long serialVersionUID = -5373220063491045170L;

			@Override
			public <T extends IExtVisitorHost<String, ? super HostStr>> Integer apply(
					String index, T host, Integer... params) {
				return 314;
			}
		});

		int i = hostA.execute(algo);
		assertEquals("hostA.execute(algo)", 42, i);

		HostStr hostQ = new HostStr("q");
		i = hostQ.execute(algo);
		assertEquals("hostQ.execute(algo)", 314, i);

		HostStr hostB = new HostStr("b");
		i = hostB.execute(algo);
		assertEquals("hostB.execute(algo)", -1, i);

		algo.setCmd("b", new IExtVisitorCmd<Integer, String, Integer, HostStr>() {

			private static final long serialVersionUID = -3589814981068523585L;

			@Override
			public <T extends IExtVisitorHost<String, ? super HostStr>> Integer apply(
					String index, T host, Integer... params) {
				return -99;
			}
		});

		i = hostB.execute(algo);
		assertEquals("hostB.execute(algo)", -99, i);
	}



	/**
	 * Test using hosts that are subclasses of a common interface. 
	 * 
	 */
	public void test_IHostStr() {
		AHostStr host1 = new HostStr1();

		HostStrVis2  algo = new HostStrVis2(); 

		algo.setCmd("HostStr1", new IExtVisitorCmd<Integer, String, Integer, AHostStr>() {

			private static final long serialVersionUID = -7876469257840081972L;

			@Override
			public <T extends IExtVisitorHost<String, ? super AHostStr>> Integer apply(
					String index, T host, Integer... params) {
				return 1234;
			}
		});

		algo.setCmd("HostStr2", new IExtVisitorCmd<Integer, String, Integer, AHostStr>() {

			private static final long serialVersionUID = -6180789499105530670L;

			@Override
			public <T extends IExtVisitorHost<String, ? super AHostStr>> Integer apply(
					String index, T host, Integer... params) {
				return 5678;
			}
		});

		int i = host1.execute(algo);
		assertEquals("host1.execute(algo)", 1234, i);

		HostStr2 host2 = new HostStr2();
		i = host2.execute(algo);
		assertEquals("host2.execute(algo)", 5678, i);

		HostStr3 host3 = new HostStr3();
		i = host3.execute(algo);
		assertEquals("host3.execute(algo)", -99, i);

		algo.setCmd("HostStr3", new IExtVisitorCmd<Integer, String, Integer, AHostStr>() {

			private static final long serialVersionUID = -2504912238717371922L;

			@Override
			public <T extends IExtVisitorHost<String, ? super AHostStr>> Integer apply(
					String index, T host, Integer... params) {
				return -987;
			}
		});

		i = host3.execute(algo);
		assertEquals("hostB.execute(algo)", -987, i);
	}


	/**
	 * Test to see if casting the host to a subclass will work to enable sub-class specific operations
	 */
	public void test_IHostStr_2() {
		AHostStr host1 = new HostStr1();

		AExtVisitor<Integer, String, Integer, AHostStr> algo = new AExtVisitor<Integer, String, Integer, AHostStr>(0){

			private static final long serialVersionUID = 1493905030695632900L;}; 

			algo.setCmd("HostStr1", new IExtVisitorCmd<Integer, String, Integer, AHostStr>() {

				private static final long serialVersionUID = -3965457739936304474L;

				@Override
				public <T extends IExtVisitorHost<String, ? super AHostStr>> Integer apply(
						String index, T host, Integer... params) {
					return ((HostStr1)host).getX();
				}
			});

			algo.setCmd("HostStr2", new IExtVisitorCmd<Integer, String, Integer, AHostStr>() {

				private static final long serialVersionUID = -9004160540686198493L;

				@Override
				public <T extends IExtVisitorHost<String, ? super AHostStr>> Integer apply(
						String index, T host, Integer... params) {
					return ((HostStr2)host).getY();
				}
			});

			int i = host1.execute(algo);
			assertEquals("host1.execute(algo)", 1111, i);

			HostStr2 host2 = new HostStr2();
			i = host2.execute(algo);
			assertEquals("host2.execute(algo)", 2222, i);

			HostStr3 host3 = new HostStr3();
			i = host3.execute(algo);
			assertEquals("host3.execute(algo)", 0, i);

			algo.setCmd("HostStr3", new IExtVisitorCmd<Integer, String, Integer, AHostStr>() {

				private static final long serialVersionUID = -43252713654409871L;

				@Override
				public <T extends IExtVisitorHost<String, ? super AHostStr>> Integer apply(
						String index, T host, Integer... params) {
					return ((HostStr3)host).getZ();
				}
			});

			i = host3.execute(algo);
			assertEquals("hostB.execute(algo)", 3333, i);
	}

	public void test_HostInt() {
		HostInt host42 = new HostInt(42);
		HostInt host99 = new HostInt(99);

		HostIntVis1  algo = new HostIntVis1(); 

		algo.setCmd(42, new IExtVisitorCmd<String, Integer, Integer, HostInt>() {

			private static final long serialVersionUID = 5938769684251512703L;

			@Override
			public <T extends IExtVisitorHost<Integer, ? super HostInt>> String apply(
					Integer index, T host, Integer... params) {
				return "Hello";
			}
		});

		assertEquals("host42.execute(algo)", "Hello", host42.execute(algo));
		assertEquals("host99.execute(algo)", "No cmd found!", host99.execute(algo));

		algo.setCmd(99, new IExtVisitorCmd<String, Integer, Integer, HostInt>() {

			private static final long serialVersionUID = 7010794674989770040L;

			@Override
			public <T extends IExtVisitorHost<Integer, ? super HostInt>> String apply(
					Integer index, T host, Integer... params) {
				return "Bye";
			}
		});

		assertEquals("host99.execute(algo)", "Bye", host99.execute(algo));

	}
}

/**
 * Example class where multiple *instances* of this one class call different
 * cases on the visitor
 */
class HostStr implements IExtVisitorHost<String, HostStr> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8549907453499601308L;
	private String s;

	public HostStr(String s) {
		this.s = s;
	}

	public <R, P> R execute(IExtVisitor<R, String, P, ? extends HostStr> algo,  @SuppressWarnings("unchecked") P... params) {
		return algo.caseAt(s, this, params);
	}  
}


