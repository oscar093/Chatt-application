package chatt;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageBuffer {

	private HashMap<String, ArrayList<Message>> waitingMessagesMap = new HashMap<String, ArrayList<Message>>();
	
	public MessageBuffer(){
	}
	
	public synchronized Message[] getMessages(String username){
		Message[] msgArray = new Message[waitingMessagesMap.get(username).size()];
		if(this.waitingMessagesMap.containsKey(username)){
			ArrayList<Message> msgArrayList = this.waitingMessagesMap.get(username);
			msgArrayList.toArray(msgArray);
		}else{
			System.out.println("Error: " + username + "finns inte");	
		}
		return msgArray;
			
	}
	
	public synchronized void addUser(String username){
		if(!this.waitingMessagesMap.containsKey(username)){
			ArrayList<Message> msgList = new ArrayList<Message>();
			this.waitingMessagesMap.put(username, msgList);
		}
	}
	
	public synchronized void addMessage(String username, Message msg){
		if(msg.getMsg() != null){
			this.waitingMessagesMap.get(username).add(msg);
		}else{
			System.out.println("Error: Null msg tillagt.");
		}		
	}
	
	public synchronized boolean isEmpty(String username){
		return this.waitingMessagesMap.get(username).isEmpty();
	}
	
	public synchronized boolean containsUser(String username){
		return this.waitingMessagesMap.containsKey(username);
	}
	
	public synchronized void removeMsg(String username, Message msg){
		if(waitingMessagesMap.get(username).contains(msg)){
			this.waitingMessagesMap.get(username).remove(msg);
		}else{
			System.out.println("Något gick fel," + msg.getMsg() +" finns inte i msgBuffer. " + username);
		}
		
	}
}
