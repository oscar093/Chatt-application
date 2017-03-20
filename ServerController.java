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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JOptionPane;

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

	private ArrayList<Connect> users = new ArrayList<Connect>(); //Alla användare skall sparas. 
	private ArrayList<ClientHandler> threads = new ArrayList<ClientHandler>(); //Alla aktiva trådar. 
	private LinkedList<Message> waitingMessages = new LinkedList<Message>();//Lagrar meddelanden som inte kommit fram
	private String alreadySentTo = "";
	private Message currentMessage = new Message();
	private ArrayList<String> onlineUsersList = new ArrayList<String>();

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
	 * ??
	 * @author Group 2
	 *
	 */
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
		private String clientID;
		private ObjectOutputStream oos;
		private ObjectInputStream ois;
//		private ArrayList<String> onlineUsersList = new ArrayList<String>();

		public ClientHandler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
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
						boolean alreadyAUser = false;
						for (Connect usrs : users) {
							if (clientID.equals(usrs.getUsername())) {
								alreadyAUser = true;
							}
						}
						if (!alreadyAUser) {
							Connect con = (Connect) object;
							users.add(con);
						}
						sui.ta_chat.append(username + " has joined the chat\n");
						log.logServerMessage(username + " has connected");
						

						if(onlineUsersList.size() > 0){
							onlineUsersList.removeAll(onlineUsersList);
						}
						
						for (ClientHandler ch : threads) {
							onlineUsersList.add(ch.getClientID());
						}
						msgWaitingForClient();

						updateUsers(onlineUsersList);

					} else if (object instanceof String) {
						clientID = (String) object;
						for (Connect usrs : users) {
							if (!clientID.equals(usrs.getUsername())) {
								Connect connect = new Connect(clientID);
								users.add(connect);
							}
						}

					} else if (object instanceof Message) {
						Message msg = (Message) object;
						if (msg.getReciever()[0].contentEquals("disconnect")) {
							sui.ta_chat.append(clientID + " has left the chat\n");
							log.logServerMessage(clientID + " is disconnected");
							
							for(Iterator<ClientHandler> it = threads.iterator(); it.hasNext(); ) {
								ClientHandler ch = it.next();
								if(ch.equals(this)) {
									it.remove();
								}
							}
							onlineUsersList.removeAll(onlineUsersList);

							for (ClientHandler ch : threads) {
								onlineUsersList.add(ch.clientID);
							}
							
							updateUsers(onlineUsersList);

						} else {
							if (currentMessage.getID() == msg.getID() && currentMessage.isSent()) {
								// Do nothing
							} else {
								currentMessage = msg;
								log.logMessage(msg, msg.getSender());
								String[] recievers = (String[]) msg.getReciever();
								for (ClientHandler ch : threads) {
									for (int i = 0; i < recievers.length; i++) {
										if (ch.getClientID().equals(recievers[i])
												&& !(msg.getRecievedBy(i).contentEquals(ch.getClientID()))) {
											ch.sendMessage(msg);
											msg.setRecievedBy(ch.getClientID());
											String sentTo = "";
											for (int j = 0; j < recievers.length; j++) {
												sentTo += recievers[j] + ", ";
											}
											if (!alreadySentTo.equals(sentTo + msg.getMsg())) {
												sui.ta_chat.append("< " + clientID + " --> " + sentTo + " > "
														+ msg.getMsg() + "\n");

												alreadySentTo = sentTo + msg.getMsg();
											}
										}
									}
								}
								Message message = msg;
								String lateRecievers = "";
								if (!msg.allSent()) {
									for (Connect c : users) {
										for (int i = 0; i < msg.getReciever().length; i++) {
											if (c.getUsername().contentEquals(recievers[i])) {
												if(!msg.isRecievedBy(c.getUsername())){
													lateRecievers += c.getUsername() + " ";

													sui.ta_chat.append("< " + clientID + " !--> " + c.getUsername()
															+ " > Message: \" " + msg.getMsg()
															+ " \" will be sent when " + c.getUsername()
															+ " is online\n");
												}
											}
										}
									}
								}
								message.setReciever(lateRecievers);
								waitingMessages.addLast(message);
							}
							currentMessage.setIsSent(true);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

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
				for (ClientHandler ch : threads) {
					ch.oos.writeObject(onlineUsersArray);
					ch.oos.flush();
				}
			} catch (IOException e) {
			}
		}

		public String getClientID() {
			return this.clientID;
		}

		public void sendMessage(String message) {
			try {
				oos.writeObject(message);
				oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void sendMessage(Message msg) {
			try {
				oos.writeObject(msg);
				oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/*
		 * Denna är till för att skicka de meddelanden som lagrats när en
		 * användare varit offline.
		 */
		public void msgWaitingForClient() {  
			if (!waitingMessages.isEmpty()) {
				for (Message m : waitingMessages) {
					for (int i = 0; i < m.getReciever().length; i++) {
						if (m.getReciever()[i].contentEquals(clientID)
						 && m.isRecievedBy(clientID)==false ) {
							this.sendMessage(m);
							m.setRecievedBy(clientID);
							sui.ta_chat.append("< A waiting message: \" ID: "+ m.getID() +" \" from: " + m.getSender()
									+ " is now sent to " + clientID + " > "
									+ m.getMsg() + "\n");
						}
					}
					if(m.allSent()){
						sui.ta_chat.append("< Message: \" ID: " + m.getID() +" \" Is now recieved by all recievers > " + m.getMsg() + " \n");
						waitingMessages.remove(m);
					}
				}
			}
		}
	}
}
