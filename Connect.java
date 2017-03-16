package chatt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class Connect implements Serializable {
	private static final long serialVersionUID = 1138894280945178915L;
	private String username;
	private LinkedList<Message> waitingMessage = new LinkedList<Message>();
	
	public Connect(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String id){
		this.username = id;
	}
	
	public void addMessage(Message message){
		waitingMessage.addLast(message);
	}
	
	public Message getMessage(){
		if(!waitingMessage.isEmpty()){
			return waitingMessage.removeFirst();
		}else{
			return null;	
		}
		
	}
	
	public boolean isEmpty(){
		return waitingMessage.isEmpty();
	}
	
}