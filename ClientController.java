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

public class ClientController {

	private MainGUI gui = new MainGUI();

	private String ip;
	private int port;
	private String username;
	private boolean isConnected = false;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public ClientController(String ip, int port) {
		this.ip = ip;
		this.port = port;
		username = JOptionPane.showInputDialog("Skriv in användarnamn");
		gui.setClientName(username);
		gui.setController(this);
		gui.setUsername(username);
		gui.display();
	}

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

	public void disconnect() {
		if (isConnected) {
			try {

				isConnected = false;
				gui.addToChat("Computer", "You are now diconnected.");
				Message msg = new Message();
				msg.setSender(username);
				msg.setReciver("disconnect");
				oos.writeObject(msg);
				oos.flush();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			gui.addToChat("Computer", "You are not even connected, connect first if you want to disconnect! ");
		}

	}

	private class Listener extends Thread {
		public void run() {
			try {
				while (true) {
					Object obj = ois.readObject();
					Message msg = (Message) obj;
					gui.addToChat(msg.getSender(), msg.getMsg());
					if (msg.getPicture() != null) {
						JOptionPane.showMessageDialog(null, "Bild skickad från " + msg.getSender());
						JOptionPane.showMessageDialog(null, msg.getPicture());
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

	public void sendChatMessage() {
		if (isConnected == true) {
			try {
				Message message = new Message();
				String reciever = JOptionPane.showInputDialog("Skriv in mottagare");
				message.setReciver(reciever);
				message.setSender(username);
				message.setText(gui.getMessageBox());
				message.setPicture();
				this.oos.writeObject(message);
				this.oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			gui.addToChat("Computer", "Cannot Connect! Press Connect!");
		}
	}
}