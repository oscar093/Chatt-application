package chatt;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * A class that contains all the necessary information for the messages 
 * that is sent between the clients
 * @author Group 2
 *
 */
public class Message implements Serializable{
	private ImageIcon picture;
	private String text;
	private String recievers, sender;
	
	/**
	 * Empty constructor for initializing a Message object
	 */
	public Message() {
		
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
	
//	private void inputText() {
//		text = JOptionPane.showInputDialog("Skriv meddelande");
//	}
	
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
			int returnVal = filechooser.showOpenDialog(null); //<<
			if(returnVal == JFileChooser.APPROVE_OPTION){
				File file = filechooser.getSelectedFile();
				BufferedImage bi;
				try{
					bi = ImageIO.read(file);
					picture = new ImageIcon(bi);
				}catch(IOException e){}
				
			}
		}
	}
	
	/**
	 * A method for setting which client the message should be sent to
	 * @param reciever A string containing the name of the client
	 */
	public void setReciever(String recievers){
		this.recievers = recievers;
		
		
	}
	
	/**
	 * A method for getting which client the message should be sent to
	 * @return A String containing the name of the client
	 */
	public String[] getReciever(){
		String[] reciever = ((String) recievers).split(" ");
		return reciever;
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
	public static void main(String[] args){
		Message msg = new Message();
	}
}