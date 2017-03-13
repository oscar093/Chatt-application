package chatt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class ChatServer {
	ServerSocket serverSocket;
	public ClientListener threadClientListener;

	public ChatServer(int port) {
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

	private class ClientListener extends Thread {

		public void run() {
			System.out.println("Server igång på port: " + serverSocket.getLocalPort());
			while (true) {
				try {

					Socket socket = serverSocket.accept();
					ClientHandler clientHandler = new ClientHandler(socket);
					System.out.println(socket.getLocalAddress());
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

		public ClientHandler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			System.out.println("Client på port: " + socket.getLocalPort());
			try {
				ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
				ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

				while (true) {
					Object object = ois.readObject();
					
					if(object instanceof String){
						clientID = (String)object;
						System.out.print(clientID + ": ");
					}
					if(object instanceof Message){
						msg = (Message)object;
						System.out.println(msg.getMsg());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args){
		ChatServer server = new ChatServer(3250);
		server.start();
	}
}