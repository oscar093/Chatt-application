package chatt;

import java.awt.event.ActionEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JOptionPane;

public class ServerController {
	private ServerSocket serverSocket;
	private ClientListener threadClientListener;
	private Thread server;
	private int port;
	private ServerUI sui = new ServerUI(this);
	private ArrayList<Connect> users = new ArrayList<Connect>(); //Alla användare skall sparas. 
	private ArrayList<ClientHandler> threads = new ArrayList<ClientHandler>(); //Alla aktiva trådar. 
	private LogHandler log;
	
	public ServerController(int port) {
		this.port = port;
		log = new LogHandler();
		try {
			this.serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		if (this.threadClientListener == null) {
			this.threadClientListener = new ClientListener();
			this.threadClientListener.start();
		}
	}
	
	public void startServer(ActionEvent evt) throws IOException {
		this.server = new Thread();
		this.serverSocket = new ServerSocket(this.port);
		this.server.start();
	}
	

	private class ClientListener extends Thread {

		public void run() {
			sui.ta_chat.append("Server igång på port: " + serverSocket.getLocalPort() + "\n");
			log.logServerMessage("Server started");
			
			while (true) {
				try {

					Socket socket = serverSocket.accept();
					ClientHandler clientHandler = new ClientHandler(socket);
					threads.add(clientHandler);
					clientHandler.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class ClientHandler extends Thread {
		private Socket socket;
		private Message msg;
		private String clientID;
		private ObjectOutputStream oos;
		private ObjectInputStream ois;

		public ClientHandler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
				oos.flush();
				ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

				while (true) {
					waitingForClient(); //Måste kolla igenom denna metoden.. 
					Object object = ois.readObject();
					
					if(object instanceof Connect) {
						String username;
						username = ((Connect) object).getUsername();
						this.clientID = username;
						users.add((Connect)object);
						sui.ta_chat.append(username + " is now connected\n");
						log.logServerMessage(username + " has connected");
						
					}else if (object instanceof String) {
						clientID = (String) object;
						for (Connect usrs : users) {
							if (!clientID.equals(usrs.getUsername())) {
								Connect connect = new Connect(clientID);
								users.add(connect);
							}
						}
						
					}else if (object instanceof Message) {
						Message msg = (Message) object;
//						msg.inputMessage(object);
						sui.ta_chat.append("< " + clientID + " --> " + msg.getReciever()+ " > " + msg.getMsg()+ "\n");
						log.logMessage(msg, msg.getSender());
					//	JOptionPane.showMessageDialog(null, msg.getPicture());
						boolean threadIsActive = false;
						for(ClientHandler ch : threads){
							if(ch.getClientID().equals(msg.getReciever())){
								ch.sendMessage(msg);
								threadIsActive = true;
							}
						}
						if(!threadIsActive){
							for(Connect c : users){
								if(c.getUsername().equals(msg.getReciever())){
									c.addMessage(msg);
								}
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		public String getClientID(){
			return this.clientID;
		}
		
		public void sendMessage(String message){
			try {
				oos.writeObject(message);
				oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void sendMessage(Message msg){
			try {
				oos.writeObject(msg);
				oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void waitingForClient(){
			for(Connect c : users){
				while(!c.isEmpty()){
					Message msg = c.getMessage();
					for (ClientHandler ch : threads){
						if(msg.getReciever().equals(ch.getClientID()));
						try {
							ch.oos.writeObject(msg);
							ch.oos.flush();
						} catch (IOException e) {
							e.printStackTrace();
						}
						
					}
					
				}
			}
		}
		
		
	}

//	public static void main(String[] args) {
//		ServerController server = new ServerController(3250);
//		server.start();
//	}
}
