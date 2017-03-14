package chatt;

import java.net.*;

import javax.swing.JOptionPane;

import java.io.*;

public class ChattClient {
	private Socket socket;
	private ObjectInputStream inStream;
	private ObjectOutputStream outStream;
	private String ip;
	private int port;
	private Message msg;
	private String ID;

	public ChattClient(String ip, int port) {
		this.ip = ip;
		this.port = port;
		ID = JOptionPane.showInputDialog("Ange klient-id: ");
		try {
			connect();
		} catch (IOException e) {
		}
	}

	public void connect() throws IOException {
		System.out.println("client connected");
		socket = new Socket(ip, port);
		// inStream = new ObjectInputStream(socket.getInputStream());
		outStream = new ObjectOutputStream(socket.getOutputStream());
		new ClientChatt().start();
	}

	private class ClientChatt extends Thread {
		public void run() {
			msg = new Message();
			try {
				outStream.writeObject(ID);
				outStream.writeObject(msg);
			} catch (IOException e) {
			}
		}
	}

	public static void main(String[] args) {
//		ChattClient client = new ChattClient("127.0.0.1", 3250);
		ChattClient client = new ChattClient("10.2.30.124", 3250);
	}
}
