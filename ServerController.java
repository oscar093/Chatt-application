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
import java.util.ArrayList;
import java.util.HashMap;
/**
 * A class for controlling the server
 * @author Group 2
 *
 */
public class ServerController {
	private ServerSocket serverSocket;
	private ClientListener threadClientListener;
	private Thread server;
	private int port;
	private ServerUI sui = new ServerUI(this);
	private ArrayList<String> onlineUsersList = new ArrayList<String>();
	private Threads threads = new Threads();
	private Users users = new Users();
	
	private MessageBuffer msgBuffer;

	private LogHandler log;
	
	/**
	 * A constructor that initializes a ServerController object
	 * @param port An int with the number of the port 
	 * @throws SecurityException
	 * @throws IOException
	 */
	public ServerController(int port) throws SecurityException, IOException {
		this.port = port;
		log = new LogHandler();
		try {
			this.serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		msgBuffer = new MessageBuffer();
	}
	
	/**
	 * A method for starting the server's thread
	 */
	public void start() {
		if (this.threadClientListener == null) {
			this.threadClientListener = new ClientListener();
			this.threadClientListener.start();
		}
	}
	
	/**
	 * A method for starting the server
	 * @param evt is not used
	 * @throws IOException
	 */
	public void startServer(ActionEvent evt) throws IOException {
		this.server = new Thread();
		this.serverSocket = new ServerSocket(this.port);
		this.server.start();
	}
	
	/**
	 * Listener for connecting clients. 
	 * @author Group 2
	 *
	 */
	private class ClientListener extends Thread {

		public void run() {
			sui.setUIText("Server igång på port: " + serverSocket.getLocalPort() + "\n");
			log.logServerMessage("Server started");

			while (true) {
				try {

					Socket socket = serverSocket.accept();
					ClientHandler clientHandler = new ClientHandler(socket);
					clientHandler.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 * Class is thread that controls input and output for Clients.
	 *
	 */
	public class ClientHandler extends Thread {
		private Socket socket;
		private String clientID;
		private ObjectOutputStream oos;
		private ObjectInputStream ois;

		/**
		 * Constructor for Clienthandler
		 * @param socket is the Clients socket.
		 */
		public ClientHandler(Socket socket) {
			this.socket = socket;
		}

		/**
		 * Method takes care of incoming and outgoing streams for clients.
		 */
		public synchronized void run() {
			try {
				oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
				oos.flush();
				ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

				while (true) {
					Object object = ois.readObject();

					if (object instanceof Connect) {
						String username;
						username = ((Connect) object).getUsername();
						this.clientID = username;
						threads.addThread(username, this);
						
						msgBuffer.addUser(username);

						
						boolean alreadyAUser = false;
						for (Connect usrs : users.getUsers()) {
							if (clientID.equals(usrs.getUsername())) {
								alreadyAUser = true;
							}
						}
						if (!alreadyAUser) {
							Connect con = (Connect) object;
							users.addUser(con);
						}
						sui.setUIText(username + " has joined the chat\n");
						log.logServerMessage(username + " has connected");
						if(onlineUsersList.size() > 0){
							onlineUsersList.removeAll(onlineUsersList);
						}						
						for(String key : threads.getKeySet()){
							onlineUsersList.add(threads.getClientHandler(key).getClientID());
						}
						for (Connect c : users.getUsers()) {
							boolean isOnline = false;
							for (String key : threads.getKeySet()) {
								if (c.getUsername().contentEquals(key)) {
									isOnline = true;
								}
							}
							if (!isOnline) {
								onlineUsersList.add("<" + c.getUsername() + ">");
							}
						}

						msgWaitingForClient();

						updateUsers(onlineUsersList);

					} else if (object instanceof String) {
						clientID = (String) object;
						for (Connect usrs : users.getUsers()) {
							if (!clientID.equals(usrs.getUsername())) {
								Connect connect = new Connect(clientID);
								users.addUser(connect);
							}
						}

					} else if (object instanceof Message) {
						Message msg = (Message) object;
						String[] recievers = msg.getReciever();
						if (msg.getMsg().contains("disconnect")) {
							disconnectUser(msg.getSender());
						} else {
							
							for (String theReciever : recievers) {
								if (threads.containsKey(theReciever)) {
									threads.getClientHandler(theReciever).sendMessage(msg);
									sui.setUIText("< " + clientID + " --> " + theReciever + " > " + msg.getMsg() + "\n");
								} else {

									if (msgBuffer.containsUser(theReciever)) {
										msgBuffer.addMessage(theReciever, msg);
										sui.setUIText("< " + clientID + " !--> " + theReciever + " > Message: \" " + msg.getMsg() + " \" will be sent when " + theReciever + " is online\n");
									} else {
										System.out.println("Mottagare: " + theReciever + " finns inte....");
									}
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

		/**
		 * Send a list to Clients with information about all online and offline users.
		 * @param list
		 */
		public void updateUsers(ArrayList<String> list) {
			int counter = 0;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) != null) {
					counter++;
				}
			}
			String[] onlineUsersArray = new String[counter];
			for (int i = 0; i < onlineUsersArray.length; i++) {
				onlineUsersArray[i] = list.get(i);
			}
			try {
				for (String key : threads.getKeySet()) {
					threads.getClientHandler(key).oos.writeObject(onlineUsersArray);
					threads.getClientHandler(key).oos.flush();
				}
			} catch (IOException e) {
			}
			
		}

		/**
		 * Returns clientID for current thread.
		 * @return
		 */
		public String getClientID() {
			return this.clientID;
		}

		/**
		 * Sends message to client.
		 * @param message
		 */
		public void sendMessage(Message msg) {
			try {
				oos.writeObject(msg);
				oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Sends message to client who where offline when message was sent. 
		 */
		public void msgWaitingForClient() {  
			if(!msgBuffer.isEmpty(clientID)){				
				for(Message msg : msgBuffer.getMessages(clientID)){
					if(msg != null){
						sendMessage(msg);
						sui.setUIText("< Buffered Message from " + msg.getSender() + " --> " + clientID + " is now sent. > " 
						+ msg.getMsg() + "\n");
						
						msgBuffer.removeMsg(clientID, msg);
					}
				}
			}		
		}

		/**
		 * Disconnects current thread.
		 */
		public void disconnectUser(String username){
			sui.setUIText(username + " has left the chat\n");
			log.logServerMessage(username + " is disconnected");
			
			threads.removeThread(username);
			onlineUsersList.removeAll(onlineUsersList);
			for (String key : threads.getKeySet()) {
				onlineUsersList.add(key);
			}
			for(Connect c : users.getUsers()){
				boolean isOnline = false;
				for(String key : threads.getKeySet()){
					if(c.getUsername().contentEquals(key)){
						isOnline = true;
					}
				}
				if(!isOnline){
					onlineUsersList.add("<" + c.getUsername() + ">");
				}	
			}
			updateUsers(onlineUsersList);
		}
	}
}
