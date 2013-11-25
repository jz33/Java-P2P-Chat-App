package jun.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JTextField;

public class ChatView extends JPanel {

	private static final long serialVersionUID = -5201786715805591405L;
	private final JSplitPane ctrlPnl = new JSplitPane();
	private final JSplitPane infoPnl = new JSplitPane();
	private final JPanel pnl_msg = new JPanel();
	private final JPanel pnl_img = new JPanel();
	private final JScrollPane pnl_output = new JScrollPane();
	private final JScrollPane pnl_userlist = new JScrollPane();		
	private final JTextField txtMsg = new JTextField();
	private final JTextField txtURL = new JTextField();
	private final JTextArea msgTA = new JTextArea();	
	private final JButton btnSendMsg = new JButton("Send Message");
	private final JButton btnBrowse = new JButton("Browse Image");
	private final JButton btnSendImg = new JButton("Send Image");
	private final JButton btnExit = new JButton("Exit Chatroom");
	private final JList<String> userlist = new JList<String>();
	private DefaultComboBoxModel<String> userlistModel = new DefaultComboBoxModel<String>();
	private IChatRoomModelAdapter model;

	public ChatView(){
		initGUI();
	}
	
	/**
	 * Constructor
	 * @param chatroommodel
	 */
	public ChatView(IChatRoomModelAdapter chatroommodel) {
		this.model = chatroommodel;
		initGUI();
	}
	
    /**************************************Methods in IChatRoomViewAdapter************************************************/
	
	public void append(String msg){
		msgTA.append(msg+"\n");
		msgTA.setCaretPosition(msgTA.getText().length());
	}
	
	public void addUser(String username){
		userlistModel.addElement(username);
	}
	
	public void removeUser(String username){
		userlistModel.removeElement(username);
		validate();
	}
	
	public void reset(){
		userlistModel.removeAllElements();
		msgTA.setText("");
	}
	
	/**************************************Chat Room GUI************************************************/

	private void initGUI() {
		/*
		setBounds(0, 0, 500, 350);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setPreferredSize(new java.awt.Dimension(400, 300));
		*/
		BorderLayout thisLayout = new BorderLayout();
		this.setLayout(thisLayout);
		this.setPreferredSize(new java.awt.Dimension(422, 300));
		/**
		 * Info panel
		 */
		this.add(infoPnl, BorderLayout.CENTER);
		infoPnl.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		infoPnl.setLeftComponent(pnl_output);
		pnl_output.setBorder(new TitledBorder(null, "Messages", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnl_output.setPreferredSize(new java.awt.Dimension(300, 200));
		{
			pnl_output.setViewportView(msgTA);
		}
		
		infoPnl.setRightComponent(pnl_userlist);
		pnl_userlist.setBorder(new TitledBorder(null, "Current Users", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnl_userlist.setPreferredSize(new java.awt.Dimension(100, 200));
		{
			pnl_userlist.setViewportView(userlist);
			userlist.setModel(userlistModel);
		}
		
		/**
		 * Control Panel
		 */
		this.add(ctrlPnl, BorderLayout.SOUTH);
		ctrlPnl.setOrientation(JSplitPane.VERTICAL_SPLIT);
		ctrlPnl.setLeftComponent(pnl_msg);
		{
			txtMsg.setText("Hi There!");
			txtMsg.setColumns(10);
			pnl_msg.add(txtMsg);
			pnl_msg.add(btnSendMsg);
			btnSendMsg.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					model.sendMsg(txtMsg.getText());					
				}
			});
			
			pnl_msg.add(btnExit);
			btnExit.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					model.exitRoom();
				}
			});
		}

		ctrlPnl.setRightComponent(pnl_img);
		{
			txtURL.setText("Local Image URL");
			txtURL.setColumns(10);
			pnl_img.add(txtURL);
			pnl_img.add(btnBrowse);
			pnl_img.add(btnSendImg);
			
			btnBrowse.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = new JFileChooser();
					if (fc.showOpenDialog(fc) == JFileChooser.APPROVE_OPTION){
						File file = fc.getSelectedFile();
				        txtURL.setText(file.getAbsolutePath());
				    }
				}
			});
			
			btnSendImg.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					model.sendImg(txtURL.getText());					
				}
			});
		}
		
		
	}
}
