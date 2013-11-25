package provided.extvisitor.test;
import provided.extvisitor.*;

/**
 * Different *sub-classes* of this interface call different methods on the visitor
 */
interface IHostStr< H extends IHostStr<? extends H>> extends IExtVisitorHost<String, H> {
}





/**
 * A lambda-based visitor to a HostStr
 */
class HostStrVis extends AExtVisitor<Integer, String, Integer, HostStr> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9219792019089879282L;

	public HostStrVis() {
		super(-1);
	} 
}


/**
 * A visitor to an IHostStr
 */
class HostStrVis2 extends AExtVisitor<Integer, String, Integer, AHostStr> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5583524977815228794L;

	public HostStrVis2() {
		super(-99);
	} 
}

/**
 * A host that uses an integer for its index
 */
class HostInt extends AExtVisitorHost<Integer, HostInt> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6913783063875505325L;

	public HostInt(int i) {
		super(i);
	}
}

/**
 * A visitor to a host that uses an integer index
 */
class HostIntVis1 extends AExtVisitor<String, Integer, Integer, HostInt> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 926232454724347039L;

	public HostIntVis1() {
		super("No cmd found!");
	} 
}
