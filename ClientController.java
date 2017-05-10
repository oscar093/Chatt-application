package chatt;

import java.awt.event.ActionEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * A class for controlling a client
 * @author Group 2
 *
 */
public class ClientController {

	private MainGUI gui = new MainGUI();

	private String ip;
	private int port;
	private String username;
	private boolean isConnected = false;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	/**
	 * A constructor that initializes a ClientController-object
	 * @param ip sets the IP-address of the client
	 * @param port sets which port the client is connecting to
	 */
	public ClientController(String ip, int port) {
		this.ip = ip;
		this.port = port;
		username = JOptionPane.showInputDialog("Skriv in användarnamn");
		gui.setClientName(username);
		gui.setController(this);
		gui.setUsername(username);
		gui.display();
	}
	
	/**
	 * A method that connects a client to the server
	 * @param evt is not used
	 */
	public void connect(ActionEvent evt) {
		if (isConnected == false) {
			try {
				socket = new Socket(ip, port);
				gui.addToChat("Computer", "Client port: " + socket.getLocalPort());
				oos = new ObjectOutputStream(socket.getOutputStream());
				oos.flush();
				ois = new ObjectInputStream(socket.getInputStream());
				isConnected = true;
				oos.writeObject(new Connect(username));
				oos.flush();
				new Listener().start();
			} catch (Exception ex) {
				gui.addToChat("Computer", "Cannot Connect! Try Again.");
			}
		} else if (isConnected == true) {
			gui.addToChat("Computer", "You are already connected.");
		}
	}
	
	/**
	 * A method that disconnects a client from the server
	 */
	public void disconnect() {
		if (isConnected) {
			try {
				isConnected = false;
				Message msg = new Message();
				msg.setSender(username);
				msg.setText("disconnect");
				oos.writeObject(msg);
				oos.flush();
				socket.close();
				gui.addToChat("Computer", "You are now diconnected.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			gui.addToChat("Computer", "You are not even connected, connect first if you want to disconnect! ");
		}

	}
	
	/**
	 * Inner class with the client's thread that listens for incoming objects
	 * @author Group 2
	 *
	 */
	private class Listener extends Thread {
		public void run() {
			try {
				while (true) {
					Object obj = ois.readObject();
					if (obj instanceof Message) {
						Message msg = (Message) obj;
						if (!msg.getMsg().isEmpty()) {
							gui.addToChat(msg.getSender(), msg.getMsg());
						}
						if (msg.getPicture() != null) {
							JOptionPane.showMessageDialog(null, "Bild skickad från " + msg.getSender());
							JOptionPane.showMessageDialog(null, msg.getPicture());
						}
					}
					if (obj instanceof String[]) {
						String[] temp = (String[]) obj;
						String[] onlineUsers = new String[temp.length];
						for(int i = 0; i < onlineUsers.length; i++){
							onlineUsers[i] = temp[i];
						}
						gui.removeAllCheckBoxes();
						for (int i = 0; i < onlineUsers.length; i++) {
							gui.addNewUserCheckBox(onlineUsers[i]);
						}
					}
				}
			} catch (Exception e) {
			}
			try {
				socket.close();
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 *  A method for sending a message from a client to the server
	 */
	public void sendChatMessage() {
		if (isConnected == true) {
			try {
				Message message = new Message();
				String reciever = this.gui.getRecievers();
				message.setReciever(reciever);
				message.setSender(username);
				message.setText(gui.getMessageBox());
				this.oos.writeObject(message);
				this.oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			gui.addToChat("Computer", "Cannot Connect! Press Connect!");
		}
	}
	
	/**
	 *  A method for sending a picture from a client to the server
	 */
	public void sendPicture(){
		if(isConnected == true){
			try{
				Message message = new Message();
				String reciever = this.gui.getRecievers();
				message.setReciever(reciever);
				message.setSender(username);
				message.setText(gui.getMessageBox());
				message.setPicture();
				this.oos.writeObject(message);
				this.oos.flush();
			} catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}