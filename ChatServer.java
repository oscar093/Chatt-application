package package1;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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

	private class ClientHandler extends Thread {
		Socket socket;
//		Message message;
		String test;

		public ClientHandler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			System.out.println("Client på port: " + socket.getLocalPort());
			try {
				ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
				ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

				while (true) {
//					while (true) { //Här skall det inte vara true, här skall det vara en buffer.isEmpty() till exempel. 
//						wait();
//					}
					test = (String) ois.readObject();
					System.out.println(test);
				}
			} catch (IOException e) {
				e.printStackTrace();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main (String[] args){
		ChatServer cs = new ChatServer(3250);
		cs.start();
	}
}
