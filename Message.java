package chatt;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * A class that contains all the necessary information for the messages 
 * that is sent between the clients
 * @author Group 2
 *
 */
public class Message implements Serializable {
	private ImageIcon picture;
	private String text;
	private String recievers, sender;
	private ArrayList<String> recievedList = new ArrayList<String>();
	private int id;
	private boolean isSent = false;

	private Random rand = new Random();
	
	/**
	 * Empty constructor for initializing a Message object
	 */

	public Message() {
		id = rand.nextInt(10000);
	}

	/**
	 * Determines weather message is sent or not.
	 * @return
	 */
	public boolean isSent() {
		return isSent;
	}

	/**
	 * Set if message is sent.
	 * @param bool
	 */
	public void setIsSent(boolean bool) {
		isSent = bool;
	}

	/**
	 * Returns message ID
	 * @return
	 */
	public int getID() {
		return id;
	}

	/**
	 * Generates a hopefully unique ID for the message.
	 */
	public void generateNewID() {
		this.id = rand.nextInt(10000);
	}
	
	/**
	 * A method for setting the text of a message
	 * @param text A String containing the text to be set
	 */
	public void setText(String text){
		this.text = text;
	}
	
	/**
	 * A method for getting the text of a message
	 * @return A String with the text in the message
	 */
	public String getMsg(){
		return this.text;
	}
	
	/**
	 * A method for getting a picture from a message
	 * @return A ImageIcon from the message
	 */
	public ImageIcon getPicture(){
		return picture;
	}
	
	/**
	 * A method for converting an object to a String
	 * @param o the object to be converted
	 */
	public void inputMessage(Object o){
		text = (String)o;
	}
	
	/**
	 * A method for creating & initialize a new instance of the class SelectPicture
	 */
	public void setPicture(){
		new SelectPicture();
	}
	
	/**
	 * An inner class for making the client select a picture from its hard drive 
	 * to be sent in a message
	 * @author Group 2
	 *
	 */
	private class SelectPicture{
		public SelectPicture(){
			FileNameExtensionFilter filter = new FileNameExtensionFilter("jpg", "png", "gif");
			JFileChooser filechooser = new JFileChooser();
			filechooser.setFileFilter(filter);
			int returnVal = filechooser.showOpenDialog(null); // <<
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = filechooser.getSelectedFile();
				BufferedImage bi;
				try {
					bi = ImageIO.read(file);
					picture = new ImageIcon(bi);
				} catch (IOException e) {
				}

			}
		}
	}

	/**
	 * Returns if message is recieved by a user.
	 * @param element in array.
	 * @return name of user.
	 */
	public String getRecievedBy(int element) {
		if (recievedList.size() > element) {
			return recievedList.get(element);
		} else {
			return "NoOneShouldHaveAIDLikeThisOne"; // Kan inte skicka null så
													// måste göra såhar.
		}
	}

	/**
	 * Returns wheather or not the message is recieved by a user.
	 * @param username of reciever
	 * @return true if recieved
	 */
	public boolean isRecievedBy(String username) {
		boolean bool = false;
		for (String strRec : recievedList) {
			if (strRec.contentEquals(username)) {
				bool = true;
			}
		}

		return bool;
	}


	/**
	 * Determin if anyone has got the message.
	 * @return
	 */
	public boolean recievedListIsEmpty() {
		return recievedList.isEmpty();
	}

	public void removeRecievedBy(String recievedBy) {
		for (int i = 0; i < recievedList.size(); i++) {
			if (recievedList.get(i).contentEquals(recievedBy)) {
				recievedList.remove(i);
			}
		}
	}

	/**
	 * Set who have received message.
	 * @param recievedBy
	 */
	public void setRecievedBy(String recievedBy) {
		recievedList.add(recievedBy);
	}

	/**
	 * A method for setting which client the message should be sent to
	 * @param reciever A string containing the name of the client
	 */
	public void setReciever(String recievers){
		this.recievers = recievers;
	}

	/**
	 * Determines if message is sent to all recievers.
	 * @return true if message is sent to all receivers.
	 */
	public boolean allSent() {
		int allSent = 0;
		for (String rec : getReciever()) {
			for (String recBy : recievedList) {
				if (rec.contentEquals(recBy)) {
					allSent += 1;
				}
			}
		}
		if (allSent >= getReciever().length) {
			return true;
		} else {
			return false;
		}

	}

	public int getRecievedByListSize() {
		return recievedList.size();
	}

	/**
	 * A method for getting which client the message should be sent to
	 * @return A String containing the name of the client
	 */
	public String[] getReciever() {
		String[] reciever = { "" };
		if (recievers != null) {
			reciever = ((String) recievers).split(" ");
			return reciever;
		} else {
			return reciever;
		}

	}
	
	/**
	 * A method for setting which client is sending the message
	 * @param sender A String containing the name of the sender
	 */
	public void setSender(String sender){
		this.sender = sender;
	}
	
	/**
	 * A method for getting which client the message was sent from 
	 * @return A String containing the name of the sender
	 */
	public String getSender(){
		return this.sender;
	}

	public static void main(String[] args) {
		Message msg = new Message();
	}
}