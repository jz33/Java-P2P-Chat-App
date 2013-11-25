package provided.rmiUtils;

/**
 * Definitions for use in RMI programs
 */
public interface IRMI_Defs {
	/**
	 * Port used for Registry.  Port 1099 is standard port.
	 * Non-standard port for RMI Registry required if used with DrJava
	 * Note that this port must be opened of inbound traffic on the server machine
	 */
	public static final int REGISTRY_PORT = 2099;

	/**
	 * Port used by the class server on the Server side.
	 */
	public static final int CLASS_SERVER_PORT_SERVER = 2001;
	
	/**
	 * Port used by the class server on the Client side.  
	 * Must be different than CLASS_SERVER_PORT_SERVER to enable
	 * both client and server to run on same machine.
	 */
	public static final int CLASS_SERVER_PORT_CLIENT = 2002;


}