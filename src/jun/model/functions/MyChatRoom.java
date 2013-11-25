package jun.model.functions;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import provided.datapacket.ADataPacket;
import comp310f13.rmiChat.IChatRoom;
import comp310f13.rmiChat.IUser;

public class MyChatRoom implements IChatRoom{

	private static final long serialVersionUID = -8812628381076673129L;
	private String name = "Default Chat Room";
	private ArrayList<MyUserProxy> proxylist = new ArrayList<MyUserProxy>();
	private ArrayList<IUser> userlist = new ArrayList<IUser>();
	
	public MyChatRoom(){}
	public MyChatRoom(String username){
		this.name = username;
	}
	
	public String getName() {
		return this.name;
	}

	public void addLocalUser(IUser newUserStub) {
		try {
			MyUserProxy proxy = new MyUserProxy(newUserStub);
			if(!proxylist.contains(proxy)) proxylist.add(proxy);
		} catch (RemoteException e) {
			System.err.println("MyChatRoom.addLocalUser(): error getting proxy user: "+ e.getMessage());
			e.printStackTrace();
		}
	}

	public void removeLocalUser(IUser userStub) {
		try {
			proxylist.remove(new MyUserProxy(userStub));
		} catch (RemoteException e) {
			System.err.println("MyChatRoom.removeLocalUser(): error getting proxy user: "+ e.getMessage());
			e.printStackTrace();
		}
	}

	public Iterable<IUser> getUsers() {
		for(MyUserProxy proxy:this.proxylist) this.userlist.add(proxy.getStub());
		return this.userlist;
	}

	/**
	 * @author swong
	 */
	public Iterable<ADataPacket> sendMessage(final ADataPacket dp) {
		ForkJoinPool taskPool = new ForkJoinPool();
		ArrayList<Callable<ADataPacket>> tasks = new ArrayList<Callable<ADataPacket>>();
		for(IUser user: proxylist) {
			final IUser taskUser = user;
			Callable<ADataPacket> task = new Callable<ADataPacket>() {

				public ADataPacket call() throws Exception {
					return taskUser.receiveData(dp);
				}				
			};
			tasks.add(task);
		}
		
		List<Future<ADataPacket>> results =  taskPool.invokeAll(tasks);
		ArrayList<ADataPacket> returnedResults = new ArrayList<ADataPacket>();
		for( Future<ADataPacket> f : results){
			try {
				returnedResults.add(f.get());
			} catch (InterruptedException e) {
				System.err.println(this.getClass().getSimpleName()+".sendMessage: "+ e.getMessage());
				e.printStackTrace();
			} catch (ExecutionException e) {
				System.err.println(this.getClass().getSimpleName()+".sendMessage: "+ e.getMessage());
				e.printStackTrace();
			}
		}
		return returnedResults;
	}
	
	public String toString(){
		return this.name;
	}

}
