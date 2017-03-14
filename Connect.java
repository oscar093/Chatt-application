package chatt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class Connect implements Serializable {
	private static final long serialVersionUID = 1138894280945178915L;
	private String username;
	private LinkedList<String> waitingMessage = new LinkedList<String>();
	
	public Connect(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String id){
		this.username = id;
	}
	
	public void addMessage(String message){
		waitingMessage.addLast(message);
	}
	
	public String getMessage(){
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