package chatt;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageBuffer {

	private HashMap<String, ArrayList<Message>> waitingMessagesMap = new HashMap<String, ArrayList<Message>>();

	/**
	 * Method returns an array of all buffered messages.
	 * @param username
	 * @return Message[]
	 */
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
	
	/**
	 * Gives the user a buffer
	 * @param username
	 */
	public synchronized void addUser(String username){
		if(!this.waitingMessagesMap.containsKey(username)){
			ArrayList<Message> msgList = new ArrayList<Message>();
			this.waitingMessagesMap.put(username, msgList);
		}
	}
	
	/**
	 * Adds message to buffer.
	 * @param username
	 * @param msg
	 */
	public synchronized void addMessage(String username, Message msg){
		if(msg.getMsg() != null){
			this.waitingMessagesMap.get(username).add(msg);
		}else{
			System.out.println("Error: Null msg tillagt.");
		}		
	}
	
	/**
	 * returns true if buffer is empty.
	 * @param username
	 * @return
	 */
	public synchronized boolean isEmpty(String username){
		return this.waitingMessagesMap.get(username).isEmpty();
	}
	
	/**
	 * Controlles if user has a buffer.
	 * @param username
	 * @return
	 */
	public synchronized boolean containsUser(String username){
		return this.waitingMessagesMap.containsKey(username);
	}
	
	/**
	 * Removes a message from buffer.
	 * @param username
	 * @param msg
	 */
	public synchronized void removeMsg(String username, Message msg){
		if(waitingMessagesMap.get(username).contains(msg)){
			this.waitingMessagesMap.get(username).remove(msg);
		}else{
			System.out.println("Något gick fel," + msg.getMsg() +" finns inte i msgBuffer. " + username);
		}
		
	}
}
