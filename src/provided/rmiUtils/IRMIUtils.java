package provided.rmiUtils;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.registry.Registry;

/**
 * Utility interface used to abstract and encapsulate the configuration
 * initialization and shutdown of RMI
 * 
 * @author swong
 * 
 */
public interface IRMIUtils {

	/**
	 * Returns the RMI Registry on the local machine on port
	 * IRMI_Defs.REGISTRY_PORT, as a server would require.
	 * 
	 * @return The local Registry
	 */
	public abstract Registry getLocalRegistry();

	/**
	 * Returns the RMI Registry on a remote machine on port
	 * IRMI_Defs.REGISTRY_PORT, as a client would require.
	 * 
	 * @param host
	 *            The IP address or host name of the remote machine
	 * @return The Registry found there.
	 */
	public abstract Registry getRemoteRegistry(String host);

	/**
	 * This method is designed to reliably return the actual local IP address
	 * across multiple platforms, particularly Linux. This method is a
	 * replacement for "java.net.InetAddress.getLocalHost().getHostAddress()"
	 * which will return the loopback address in Linux, not the actual IP
	 * address. This method only returns IPv4 addresses, not IPv6 addresses. It
	 * returns the first valid address found.
	 * 
	 * @return The first IPv4 address found for the system that is not a
	 *         loopback address.
	 * @throws SocketException
	 *             thrown when there is a problem retrieving the network
	 *             interfaces.
	 * @throws UnknownHostException
	 *             thrown when the local host address cannot be found.
	 */
	public abstract String getLocalAddress() throws SocketException,
			UnknownHostException;


	/**
	 * Start up the RMI system.   This method should be called 
	 * before any other activity with that involves RMI.   This method
	 * configures the system properties RMI needs, starts the security
	 * manager and starts the class server. <br/>
	 * Typical classServerPort values are: <br/>
	 * IRMI_Defs.CLASS_SERVER_PORT_SERVER and <br/>
	 * IRMI_Defs.CLASS_SERVER_PORT_CLIENT
	 * @param classServerPort the port that the class server will use
	 */
	public abstract void startRMI(int classServerPort);

	/**
	 * Performs all necessary work to shut the RMI system down,
	 * such as shut the class server down.   
	 * This method MUST be called before exiting the system or phantom 
	 * processes may persist. 
	 */
	public abstract void stopRMI();

}