package jun.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import provided.util.IVoidLambda;

public class AppView<HostType,ModelType> extends JFrame {

	private static final long serialVersionUID = 7846886575758174985L;
	private JPanel contentPane;
	private final JPanel ctrlPnl = new JPanel();
	private final JPanel pnl_login = new JPanel();
	private final JPanel pnl_makeroom = new JPanel();
	private final JPanel pnl_connect = new JPanel();
	private final JPanel pnl_invite = new JPanel();
	private final JPanel pnl_close = new JPanel();
	private final JTextField txtUsername = new JTextField();
	private final JTextField txtChatroom = new JTextField();
	private final JTextField txtIP = new JTextField();
	private final JButton btnLogin = new JButton("Login");
	private final JButton btnCreateChatroom = new JButton("Create Chatroom");
	private final JButton btnConnect = new JButton("Connect");
	private final JButton btnInvite = new JButton("Invite");
	private final JButton btnLogout = new JButton("Logout");	
	private final JComboBox<HostType> comboInvite = new JComboBox<HostType>();
	private final JTabbedPane contPnl = new JTabbedPane(JTabbedPane.TOP);
	private final JScrollPane scrollPane = new JScrollPane();
	private final JTextArea infoTA = new JTextArea();
	private IModelAdapter<HostType> model;

	/**
	 * Constructor & starter
	 */
	public AppView(IModelAdapter<HostType> model) {
		this.model = model;
		initGUI();
	}

	public void start(){
		setVisible(true);
	}
	
	/*********************************Connect to mini MVC************************************/
	
	public ChatView setChatRoomAdapter(String chatroomName, IChatRoomModelAdapter chatroomModel){
		ChatView chatview = new ChatView(chatroomModel);
		contPnl.insertTab(chatroomName, null, chatview, null, 0);
		contPnl.setSelectedIndex(0);
		return chatview;
	}
	
    /*********************************Methods in IViewAdapter************************************/
	/**
	 * Append some connection info
	 * @param s
	 */
	public void append(String s) {
		infoTA.append(s+"\n");
		infoTA.setCaretPosition(infoTA.getText().length());  
	}
	
	/**
	 * Add chatroom by adding a new tab 
	 * @param name
	 * @param component
	 * @return
	 */
	public IVoidLambda<Void> addComponent(String name, Component component) {
		final JScrollPane sPane = new JScrollPane();
		contPnl.addTab("Chatroom: "+name, null, sPane, null);
		sPane.setViewportView(component);
		
		return new IVoidLambda<Void>() {

			public void apply(Void... params) {
				contPnl.remove(sPane);
			}
		};
	}
	
	/**
	 * Set remote host list
	 * @param hosts
	 */
	public void setConnectedHosts(Iterable<HostType> hosts) {
		comboInvite.removeAll();
		for(HostType host: hosts) comboInvite.addItem(host);
	}
	
	/**
	 * Pop a select chat room dialog window
	 * @param roomlist
	 * @return
	 */
	public Object showRoomSelector(Collection<ModelType> roomlist) {
		return JOptionPane.showInputDialog(
				this, 
				"Please choose your chat room:", 
				"Select chat room", 
				JOptionPane.PLAIN_MESSAGE, 
				null,
				roomlist.toArray(), 
				0);
	}
	
	/**
	 * Accept invitation
	 * @param chatroomInfo
	 * @return
	 */
	public boolean acceptInvite(final String chatroomInfo){
		final boolean[] result = new boolean[1];
		final AppView<HostType,ModelType> helper = this;
		try {
			SwingUtilities.invokeAndWait(new Runnable(){

				public void run() {
					result[0] = 0 ==JOptionPane.showConfirmDialog(
							helper, 
							chatroomInfo, 
							"Chat Room Invitation", 
							JOptionPane.YES_NO_OPTION
							);
				}
				
			});
		} catch (InvocationTargetException e) {
			System.err.println(this.getClass().getSimpleName()+".accpetInvite: error to select room : "+e.getMessage());
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			System.err.println(this.getClass().getSimpleName()+".accpetInvite: error to select room : "+e.getMessage());
			e.printStackTrace();
			return false;
		}
		return result[0];
	}
	
	/*********************************Methods Auxiliary************************************/
	/**
	 * Remove chat room by removing tab
	 * @param chatroom
	 */
	public void exitRoom(ChatView chatroom) {
		int chatroomID = contPnl.indexOfComponent(chatroom);
		contPnl.removeTabAt(chatroomID);
		this.append("Exit Chat Room: "+chatroomID+" Success!\n");
	}
		
	/*********************************GUI************************************/
	private void initGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Jun's Remote Chat App");
		this.setPreferredSize(new Dimension(2000, 80));
		setBounds(100, 100, 1000, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		/**
		 * Control panel
		 */
		contentPane.add(ctrlPnl, BorderLayout.NORTH);
		ctrlPnl.setBackground(new Color(144, 238, 144));
		
		/**
		 * Login panel
		 */
		ctrlPnl.add(pnl_login);
		pnl_login.setBackground(new Color(176, 224, 230));
		pnl_login.setBorder(new TitledBorder(null, "Username", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnl_login.setLayout(new BoxLayout(pnl_login, BoxLayout.Y_AXIS));
		{
			pnl_login.add(txtUsername);
			txtUsername.setText("Jun");
			txtUsername.setColumns(10);
			pnl_login.add(btnLogin);
			
			btnLogin.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					btnLogin.setEnabled(false);
					String localaddress = model.login(txtUsername.getText());
					txtUsername.setEnabled(false);
					btnCreateChatroom.setEnabled(true);
					btnConnect.setEnabled(true);
					txtIP.setEnabled(true);
					btnLogout.setEnabled(true);
					txtIP.setText(localaddress);
					setTitle(getTitle() + " on " + localaddress);
				}
				
			});
		}
		
		/**
		 * Make chatroom panel
		 */
		ctrlPnl.add(pnl_makeroom);
		pnl_makeroom.setBackground(new Color(175, 238, 238));
		pnl_makeroom.setBorder(new TitledBorder(null, "Chat Room Name:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnl_makeroom.setLayout(new BoxLayout(pnl_makeroom, BoxLayout.Y_AXIS));
		{
			pnl_makeroom.add(txtChatroom);
			txtChatroom.setText("Jun's Chatroom");
			txtChatroom.setColumns(10);		
			pnl_makeroom.add(btnCreateChatroom);
			btnCreateChatroom.setEnabled(false);
			btnCreateChatroom.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					model.createRoom(txtChatroom.getText());
				}
				
			});
		}
		
		/**
		 * Connect to Remote Host panel
		 */
		ctrlPnl.add(pnl_connect);
		pnl_connect.setBackground(new Color(175, 238, 238));
		pnl_connect.setBorder(new TitledBorder(null, "Connect Remote Host:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnl_connect.setLayout(new BoxLayout(pnl_connect, BoxLayout.Y_AXIS));
		{
			pnl_connect.add(txtIP);
			txtIP.setEnabled(false);
			txtIP.setText("localhost");
			txtIP.setColumns(10);		
			pnl_connect.add(btnConnect);
			btnConnect.setEnabled(false);
			btnConnect.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					model.connect(txtIP.getText());
				}
				
			});
		}
		
		/**
		 * Invite Panel
		 */
		ctrlPnl.add(pnl_invite);
		pnl_invite.setBackground(new Color(175, 238, 238));
		pnl_invite.setBorder(new TitledBorder(null, "Invite:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnl_invite.setLayout(new BoxLayout(pnl_invite, BoxLayout.Y_AXIS));		
		{
			pnl_invite.add(comboInvite);
			pnl_invite.add(btnInvite);
			btnInvite.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					model.invite(comboInvite.getItemAt(comboInvite.getSelectedIndex()));
				}
				
			});
		}
		
		/**
		 * Close Panel
		 */
		ctrlPnl.add(pnl_close);
		pnl_close.setBackground(new Color(175, 238, 238));
		pnl_close.setBorder(new TitledBorder(null, "Logout/Close:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnl_close.setLayout(new BoxLayout(pnl_close, BoxLayout.Y_AXIS));		
		{
			pnl_close.add(btnLogout);
			btnLogout.setEnabled(false);
			
			btnLogout.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					model.logout();
				}
				
			});
		}
		
		/**
		 * Tab Panel
		 */
		contentPane.add(contPnl, BorderLayout.CENTER);
		contPnl.addTab("Info Panel", null, scrollPane, null);
		scrollPane.setViewportView(infoTA);
	}
}
