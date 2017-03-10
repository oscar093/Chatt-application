package chatt;

import java.net.*;
import java.io.*;

public class ChattClient {
	private Socket socket;
	private ObjectInputStream inStream;
	private ObjectOutputStream outStream;
	private String ip;
	private int port;
	private Message msg;

	public ChattClient(String ip, int port) {// (String ip, int port){
		this.ip = ip;
		this.port = port;

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
					outStream.writeObject(msg);
				} catch (IOException e) {
				}
		}
	}
}
