package provided.datapacket;

import java.awt.Component;

import comp310f13.rmiChat.IUser;

/**
 * Interface between a DataPacketAlgoCmd and the model of a ChatApp system.   Allows a foreign command
 * access to some limited services of its host ChatApp.  
 */
public interface ICmd2ModelAdapter {
	/**
	 * Gets a reference to the local IUser stub which is needed to instantiate status packets for return objects.
	 * @return The local IUser's stub.
	 */
	public abstract IUser getLocalUserStub();
	
	/**
	 * Appends the given string onto a text display somewhere on the host ChatApp's GUI.
	 * @param s A string to display
	 */
	public abstract void append(String s);
	
	/**
	 * Allows the command to give a java.awt.Component to the host ChatApp to be displayed on 
	 * its GUI somewhere. 
	 * @param name  A title for the component, useful for tabs, frame titles, etc.
	 * @param newComp  The component to display.
	 */
	public void addComponent(String name, Component newComp);
}
