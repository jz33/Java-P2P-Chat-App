package provided.extvisitor;

import java.util.*;

/**
 * Abstract implementation of IExtVisitor that adds an invariant implementation of 
 * storing commands associated with each case in a dictionary indexed by the 
 * case's associated index value.   
 * When a particular case is called, the associated command is retrieved and 
 * executed.  The return value is the return value from the command.
 * If no associated command is found, then a default command is executed.
 * In general, command-based implementations of IExtVisitor will be concrete subclasses of this class.
 * <br/>Usage:<pre>  
 * public class MyExtVisitor extends AExtVisitor&lt;MyReturn, MyIndex, MyReturn, MyExtVisitorHost&gt; {...}
 * </pre>
 * @param <R> The type of the return value
 * @param <I> The type of the index value
 * @param <P> The type of the input parameters
 * @param <H> The type of the host, restricted to being a subclass of IExtVisitorHost&lt;I, ? super H&gt;
 * @author Stephen Wong (c) 2010 * ----------------------------------------------
 * Adds invariant command management and caseAt() behavior.
 */
public abstract class AExtVisitor<R, I, P, H extends IExtVisitorHost<I, ? super H> > implements IExtVisitor<R,I,P, H> {

	private static final long serialVersionUID = 4445948668748598430L;

	/**
	 * The dictionary used to store the commands
	 */
	private Map<I, IExtVisitorCmd<R, I, P, H>> cmds = new Hashtable<I, IExtVisitorCmd<R, I, P,  H>>();

	/**
	 * The default command to use if no command is associated with a case index value.
	 */
	private IExtVisitorCmd<R, I, P, H> defaultCmd;

	/**
	 * Constructor that takes a default command to use.
	 * @param defaultCmd  The default command to use.
	 */
	public AExtVisitor(IExtVisitorCmd<R, I, P, H> defaultCmd) {
		this.defaultCmd = defaultCmd;
	}

	/**
	 * A convenience constructor that takes a value that the default command will return.  
	 * A default command is created will return the given value and do nothing else.
	 * @param noOpResult The value for the default command to return.
	 */
	public AExtVisitor(final R noOpResult) {
		this(new IExtVisitorCmd<R, I, P, H>() {

			private static final long serialVersionUID = -3773477471593844489L;

			public <T extends IExtVisitorHost<I, ? super H>> R apply(I index, T host, @SuppressWarnings("unchecked") P... params) {
				return noOpResult;
			}
		});
	}

	/**
	 * Associates the given index value with the given command
	 * @param idx The index value to use associate with the command.
	 * @param cmd The command associated with the index value
	 */
	public void setCmd(I idx, IExtVisitorCmd<R, I, P, H> cmd) {
		cmds.put(idx, cmd);
	}

	/**
	 * Retrieve the command associated with given index value.
	 * null is returned if no command is associated with the index value.
	 * @param idx  An index value
	 * @return The IExtVisitorCmd associated with the index value or null
	 */
	public IExtVisitorCmd<R, I, P, H> getCmd(I idx) {
		return cmds.get(idx);
	}

	/**
	 * Retrieve the current default command
	 * @return The current default command
	 */
	public IExtVisitorCmd<R, I, P, H> getDefaultCmd(){
		return defaultCmd;
	}

	/**
	 * Set the default command to a new value.
	 * @param defaultCmd The new default command
	 */
	public void setDefaultCmd(IExtVisitorCmd<R, I, P, H> defaultCmd) {
		this.defaultCmd = defaultCmd;
	}

	/**
	 * Concrete implementation of the parameterized case method that takes 
	 * the index value, retrieves an associated IExtVisitor command and 
	 * executes the command with the given host and input parameters.   
	 * The result from the command execution is returned.   If not associated
	 * command is found, then the current default command is executed.
	 * Any given host's execute method will call this method, passing the index value 
	 * associated with that host, a reference to the host itself and pass along any 
	 * input parameters that were supplied. 
	 * @param <T> The type of the host the is expected to call this method.  T is restricted to be a subclass of IExtVisitorHost&lt;I, ? super H&gt;
	 * @param idx The index value for the case
	 * @param host The visitor's host.
	 * @param params Vararg input parameters for the case
	 * @return The result from executing the associated or default command
	 */
	public  <T extends IExtVisitorHost<I, ? super H>> R caseAt(I idx, T host, @SuppressWarnings("unchecked") P... params) {
		IExtVisitorCmd<R, I, P, H> cmd = cmds.get(idx);
		if(cmd == null) return defaultCmd.apply(idx, host, params);
		else return cmd.apply(idx, host, params);
	}

}