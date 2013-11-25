package provided.extvisitor;

/**
 * Abstract implementation of IExtVisitorHost that provides defines a host by an index value and thus, 
 * a concrete execute method.    
 * 
 * <br/>Usage:<pre>   
 * public class MyExtVisitorHost extends AExtVisitorHost&lt;MyIndex, MyExtVisitorHost&gt; {...}
 * </pre>
 * @param <I>  The type of the index value that is being used.
 * @param <H>  The type of the concrete SUBCLASS that extends this class.
 * @author Stephen Wong (c) 2010 * ----------------------------------------------
 * Adds invariant execute() behavior:
 * return algo.caseAt(idx, this, params);
 */
public abstract class AExtVisitorHost<I, H extends IExtVisitorHost<I, ? extends H>>  implements IExtVisitorHost<I, H> {

	private static final long serialVersionUID = -7702178780705773404L;

	/**
	 * The index value that is associated with this host object
	 */
	private I idx;

	/**
	 * Constructor for the class.
	 * @param idx  The index value that is associated with this host object
	 */
	public AExtVisitorHost(I idx) {
		this.idx = idx;
	}

	/**
	 * "Accept" method of the visitor design pattern that executes a given extended visitor by calling the associated
	 * indexed case of the visitor. 
	 * Returns algo.caseAt(idx, this, params)  where idx is the index value stored in this host.
	 * 
	 * @param algo   The extended visitor to run, which must use the same index type as this host, and be able to run on a sub-class of this host.
	 * @param params  Vararg input parameter to be used by the visitor.
	 * @return  The result of running the associated indexed case of the visitor method.
	 */
	public <R, P> R execute(IExtVisitor<R, I, P, ? extends H> algo,  @SuppressWarnings("unchecked") P... params) {
		return algo.caseAt(idx, this, params);
	}

}