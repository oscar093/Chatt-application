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

public class ServerController {
	private ServerSocket serverSocket;
	private ClientListener threadClientListener;
	private Thread server;
	private int port;
	private ServerUI sui = new ServerUI(this);
	private ArrayList<Connect> users = new ArrayList<Connect>(); // Alla
																	// användare
																	// skall
																	// sparas.
	private ArrayList<ClientHandler> threads = new ArrayList<ClientHandler>(); // Alla
																				// aktiva
																				// trådar.
	private LinkedList<Message> waitingMessages = new LinkedList<Message>();// Lagrar
																			// meddelanden
																			// som
																			// inte
																			// kommit
																			// fram
	private ArrayList<String> onlineUsersList = new ArrayList<String>();

	private LogHandler log;

	public ServerController(int port) throws SecurityException, IOException {
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
						msgWaitingForClient(); // Denna metoden är inte färdig!

						if(onlineUsersList.size() > 0){
							onlineUsersList.removeAll(onlineUsersList);
						}
						
						for (ClientHandler ch : threads) {
							onlineUsersList.add(ch.getClientID());
						}

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
						if (msg.getMsg().contentEquals("disconnect")) {
							sui.ta_chat.append(clientID + " has left the chat\n");
							log.logServerMessage(clientID + " is disconnected");

							for (Iterator<ClientHandler> it = threads.iterator(); it.hasNext();) {
								ClientHandler ch = it.next();
								if (ch.equals(this)) {
									it.remove();
								}
							}
							onlineUsersList.removeAll(onlineUsersList);

							for (ClientHandler ch : threads) {
								onlineUsersList.add(ch.clientID);
							}
							
							updateUsers(onlineUsersList);

						} else {
							log.logMessage(msg, msg.getSender());
							boolean threadIsActive = true;
							String[] recievers = (String[]) msg.getReciever();
							for (int i = 0; i < recievers.length; i++) {
								for (ClientHandler ch : threads) {
									if (ch.getClientID().equals(recievers[i])) {
										ch.sendMessage(msg);
										if (threadIsActive) {
											String sentTo = "";
											for (int j = 0; j < recievers.length; j++) {
												sentTo += recievers[j] + ", ";
											}
											sui.ta_chat.append(
													"< " + clientID + " --> " + sentTo + " > " + msg.getMsg() + "\n");
											threadIsActive = false;
										}
									}
									if (!threadIsActive) { // Den skall inte in
															// här om
															// meddelandet
															// skickats iväg,
															// men den gör det
															// iaf. Detta skall
															// fixa.
										Message message = msg;
										message.setReciever(ch.getClientID());
										waitingMessages.addLast(message);
										sui.ta_chat.append("< " + clientID + " !--> " + recievers[i] + " > Message: \" "
												+ msg.getMsg() + "\"will be sent when " + recievers[i]
												+ " is online\n");
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
		 * Denna är till för att skicka meddelanden som ligger buffrade i
		 * connected klassen.
		 */

		public void msgWaitingForClient() {
			while (!waitingMessages.isEmpty()) {
				for (ClientHandler ch : threads) {
					if (waitingMessages.getFirst().getReciever().equals(ch.getClientID())) {
						sui.ta_chat.append("< A waiting message from: " + waitingMessages.getFirst().getSender()
								+ " is now sent to " + ch.getClientID() + " > " + msg.getMsg() + "\n");
						sendMessage(waitingMessages.removeFirst());

					}

				}
			}
		}
	}
}
