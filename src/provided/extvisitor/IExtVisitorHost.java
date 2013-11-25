package provided.extvisitor;

import java.io.Serializable;

/**
 * An interface defining a host for an IExtVisitor who can visit this type of host.
 * Each concrete host will have a particular index value associated with it and call
 * the case associated with that index value on the visitor.
 * @param <I> The type of the index value used by the extended visitor.
 * @param <H> The type of the host used by the extended visitor.  Restricted to be an implementation of IExtVisitorHost&lt;I, ? extends H&gt;
 * 
 */
/**
 * Top-level extended visitor interfaces.  Completely implementation-independent. */public abstract interface IExtVisitorHost<I, H extends IExtVisitorHost<I,? extends H>> extends Serializable{
  /**
   * The method that executes (accepts) the extended visitor to process this host.
   * @param <R> The return type of the visitor.
   * @param <P> The type of the input parameters of the visitor.
   * @param algo The extended visitor to execute.
   * @param params  Vararg input parameters to the visitor.
   * @return The return value from executing the visitor.
   */
  public <R, P> R execute(IExtVisitor<R, I, P, ? extends H> algo,  @SuppressWarnings("unchecked") P... params);
}