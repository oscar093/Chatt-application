package chatt;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

/**
 * A class that represents the GUI for the clients
 * @author Group 2
 *
 */
public class MainGUI {
    private String appName = "ChattClient";
    private JFrame newFrame = new JFrame(appName);
    private JButton sendMessage;
    private JTextField messageBox;
    private JTextArea chatBox;
    private JTextField usernameChooser;
    private JFrame preFrame;
    private JButton connect;
    private JButton disConnect;
    private JButton sendPicture;
    private JPanel eastPanel;
    private ArrayList<JCheckBox> CBList = new ArrayList<JCheckBox>();
    
    private ClientController cc;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager
                            .getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MainGUI mainGUI = new MainGUI();
            }
        });
    }
    
    
    /**
     * A method for showing the display for the client
     * This is the display where the different clients communicates with each other
     */
    public void display() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.BLACK);
        southPanel.setLayout(new GridBagLayout());
        
        JPanel northPanel = new JPanel();
        northPanel.setBackground(Color.WHITE);
        northPanel.setLayout(new GridBagLayout());
        
        eastPanel = new JPanel();
        eastPanel.setBackground(Color.WHITE);
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.PAGE_AXIS));
        eastPanel.setPreferredSize(new Dimension(100, 100));
        
        messageBox = new JTextField(30);
        messageBox.requestFocusInWindow();

        sendMessage = new JButton("Send Message");
        sendMessage.addActionListener((ActionListener) new sendMessageButtonListener());
        
        connect = new JButton("Connect");
        connect.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				cc.connect(e);	
			}
        });
        
        disConnect = new JButton("Disconnect");
        disConnect.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				cc.disconnect();
			}
        });
        
        sendPicture = new JButton("Picture");
        sendPicture.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				cc.sendPicture();
			}
        });

        chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
        chatBox.setLineWrap(true);

        mainPanel.add(new JScrollPane(chatBox), BorderLayout.CENTER);

        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.LINE_START;
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 512.0D;
        left.weighty = 1.0D;

        GridBagConstraints right = new GridBagConstraints();
        right.insets = new Insets(0, 10, 0, 0);
        right.anchor = GridBagConstraints.LINE_END;
        right.fill = GridBagConstraints.NONE;
        right.weightx = 1.0D;
        right.weighty = 1.0D;

        southPanel.add(messageBox, left);
        southPanel.add(sendMessage, right);
        southPanel.add(sendPicture,right);
        
        northPanel.add(connect);
        northPanel.add(disConnect);    

        mainPanel.add(BorderLayout.SOUTH, southPanel);
        mainPanel.add(BorderLayout.NORTH, northPanel);
        mainPanel.add(BorderLayout.EAST, eastPanel);
        

        newFrame.add(mainPanel);
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setSize(500, 400);
        newFrame.setVisible(true);
    }
    
    /**
     * A inner class that listens if the "sendMessage" button is pressed
     * @author Group 2
     *
     */
    class sendMessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (messageBox.getText().length() < 1) {
                // do nothing
            } else if (messageBox.getText().equals(".clear")) {
                chatBox.setText("Cleared all messages\n");
                messageBox.setText("");
            } else {
                chatBox.append("<" + username + ">:  " + messageBox.getText()
                        + "\n");
                cc.sendChatMessage();
                messageBox.setText("");
            }
            messageBox.requestFocusInWindow();
        }
    }

    String  username;
    
    /**
     * A inner class that listens if the "connect" button is pressed
     * @author Group 2
     *
     */
    class enterServerButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            username = usernameChooser.getText();
            if (username.length() < 1) {
                System.out.println("No!");
            } else {
                preFrame.setVisible(false);
                display();
            }
        }
    }
    
    /**
     * A inner class that listens if the "sendPicture" button is pressed
     * @author Group 2
     *
     */
    class sendPictureButtonListener implements ActionListener {
    	public void actionPerformed(ActionEvent e){
    		if(e.getSource()==sendPicture){
    			cc.sendPicture();
    		}
    	}
    }
    
	/**
	 * Reads wish CheckBoxes are selected and returns a string with the recievers. 
	 * @return String recievers
	 */
	public String getRecievers() {
		String recievers = "";
		for (JCheckBox cb : CBList) {
			if(cb.isSelected()){
				if(cb.getText().endsWith(">")){
					recievers += cb.getText().substring(1, cb.getText().length()-1) + " ";
				}else{
					recievers += cb.getText() + " ";
				}
			}	
		}
		return recievers;

	}
    
    /**
     * A method for adding a username and a message to the chat window
     * @param username, name of the user that sent the message
     * @param message, text that the user sent 
     * 
     */
    public void addToChat(String username, String message){
    	chatBox.append("<" + username + ">:  " + message + "\n");
    	messageBox.setText("");
    }
    
    /**
     * A method for adding a message to the chat window
     * @param message A String containing the text that a user sent
     * 
     */
    public void addToChat(String message){
    	chatBox.append("<" + username + ">:  " + message + "\n");
    	messageBox.setText("");
    }
    
    /**
     * A method for setting a username
     * @param username A String containing the name of the user
     * 
     */
    public void setUsername(String username){
    	this.username = username;
    }
    
    /**
     * A method for setting a ClientController
     * @param cc the ClientController to be set
     * 
     */
    public void setController(ClientController cc){
    	this.cc = cc;
    }
    
    /**
     * A method for getting text from the text field
     * @return a String containing text from text field
     * 
     */
    public String getMessageBox(){
    	return messageBox.getText();
    }
    
    /**
     * A method for setting a name for the client
     * @param name A String containing the name to be set 
     * 
     */
    public void setClientName(String name){
    	this.newFrame.setTitle(name);
    }
    
    /**
     * A method for adding check-boxes
     * @param username name for the check-box
     */
    public void addNewUserCheckBox(String username){
    	JCheckBox onlineCB = new JCheckBox(username); 
    	CBList.add(onlineCB);
    	eastPanel.add(onlineCB);
    	eastPanel.updateUI();
    }
    
    /**
     * A method for removing all the check-boxes in the UI
     */
    public void removeAllCheckBoxes(){
    	CBList.clear();
    	eastPanel.removeAll();
    	eastPanel.updateUI();
    }
}