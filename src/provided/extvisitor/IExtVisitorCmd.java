package provided.extvisitor;

import java.io.Serializable;


/**
 * Interface that defines a command used by AExtVisitor that has specific 
 * types for its return value, R, its index value, I, its input parameters, P, 
 * and its host, H.   The host is restricted to being a subclass of 
 * IExtVisitorHost who takes the same index value and who accepts a visitor 
 * that takes this same host type.
 * An IExtVisitorCmd is associated with every recognized case of an AExtVisitor,
 * including the default case.
 * <br/>Usage:<pre> 
 * new IExtVisitorCmd&lt;MyReturn, MyIndex, MyParam, MyExtVisitorHost&gt;(){
 *     public &lt;T extends IExtVisitorHost&lt;Integer, ? super MyExtVisitorHost&gt;&gt; MyReturn apply(MyIndex index, T host, MyParam... params) {...}
 * } 
 * </pre>
 * @param <R> The type of the return value
 * @param <I> The type of the index value
 * @param <P> The type of the input parameters
 * @param <H> The type of the host, restricted to being a subclass of IExtVisitorHost&lt;I, ? super H&gt;
 * @author Stephen Wong (c) 2010
 */
/**
 * Implementation-independent command */public abstract interface IExtVisitorCmd<R, I, P, H extends IExtVisitorHost<I,? super H>> extends Serializable{  
  /**
   * The method that is run by AExtVisitor when the case associated with this 
   * command is executed.
   * @param <T> The type of the host the is expected to call this method.  T is restricted to be a subclass of IExtVisitorHost&lt;I, ? super H&gt;
   * @param index The index value for the case for which this command is associated. 
   * @param host The host for the visitor
   * @param params Vararg input parameters
   * @return The value returned by the running this command.
   */
  public abstract <T extends IExtVisitorHost<I, ? super H>> R apply(I index, T host, @SuppressWarnings("unchecked") P... params);
}