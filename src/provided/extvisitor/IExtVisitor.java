package provided.extvisitor;

import java.io.Serializable;

/**
 * Interface that defines an extended visitor that has specific types for its
 * return value, R, its index value, I, its input parameters, P, and its
 * host, H.   The host is restricted to being a subclass of IExtVisitorHost 
 * who takes the same index value and who accepts a visitor that takes this 
 * same host type.
 * @param <R> The type of the return value
 * @param <I> The type of the index value
 * @param <P> The type of the input parameters
 * @param <H> The type of the host, restricted to being a subclass of IExtVisitorHost&lt;I, H&gt;
 * @author Stephen Wong (c) 2010
 */
/**
 * Top-level extended visitor interfaces.  Completely implementation-independent. */public abstract interface IExtVisitor<R, I, P, H extends IExtVisitorHost<I, ? super H>> extends Serializable{
	/**
	 * The parameterized case of the visitor.  The case is parameterized by the index value, idx.
	 * @param <T> The type of the host the is expected to call this method.  T is restricted to be a subclass of IExtVisitorHost&lt;I, ? super H&gt;
	 * @param idx The index value for the desired case
	 * @param host The host for the visitor
	 * @param params Vararg input parameters
	 * @return The value returned by the running the indexed case.
	 */
	public <T extends IExtVisitorHost<I, ? super H>> R caseAt( I idx, T host, @SuppressWarnings("unchecked") P... params);
}