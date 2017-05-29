package chatt;

import java.util.HashMap;

import chatt.ServerController.ClientHandler;
/**
 * Class for all the threads
 */

public class Threads {
	private HashMap<String, ClientHandler> threadMap = new HashMap<String, ClientHandler>();
	
	/**
	 * Adds a thread to the hashmap
	 * 
	 * @param username
	 * @param ch
	 */
	public synchronized void addThread(String username, ClientHandler ch){
		threadMap.put(username, ch);
	}
	
	/**
	 * returns an array with the keySets from the hashmap
	 * 
	 * @return the array with the keysets
	 */
	public synchronized String[] getKeySet(){
		String[] arr = new String[threadMap.keySet().size()];
		threadMap.keySet().toArray(arr);
		return arr;
	}

	/**
	 * checks if the hashmap contains a given key
	 * 
	 * @param key = given key
	 * @return true or false
	 */
	public synchronized boolean containsKey(String key){
		return threadMap.containsKey(key);
	}
	
	/**
	 * returns the clienthandler connected to given key
	 * 
	 * @param key = given key
	 * @return ClientHandler
	 */
	public synchronized ClientHandler getClientHandler(String key){
		return threadMap.get(key);
	}
	/**
	 * removes thread connected to given username
	 * 
	 * @param username = given username
	 */
	
	public synchronized void removeThread(String username){
		threadMap.remove(username);
	}
}
