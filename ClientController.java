package chatt;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientController {
	
	private ClientUI cui = new ClientUI(this);
	
	private String ip;
	private int port;
	private String username;
	private boolean isConnected = false;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public ClientController(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	public void connect(ActionEvent evt) {
        if(isConnected == false) {
            username = cui.tf_username.getText();
            cui.tf_username.setEditable(false);
            try {
                socket = new Socket(ip, port);
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
                isConnected = true;
                oos.writeObject(new Connect(username));
                oos.flush();
                new Listener().start();
            } 
            catch(Exception ex) {
                cui.ta_chat.append("Cannot Connect! Try Again.\n");
                cui.tf_username.setEditable(true);
            }            
        } else if(isConnected == true) {
            cui.ta_chat.append("You are already connected.\n");
        }
    }
	
	private class Listener extends Thread {
		public void run() {
			try {
				while(true) {
					
				}
			} catch(Exception e) {}
			try {
				socket.close();
			} catch(Exception e) {}
		}
	}
}