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

public class Message implements Serializable{
	private ImageIcon picture;
	private String text;
	private String recievers, sender;
	

	public Message() {
		
	}
	
	public void setText(String text){
		this.text = text;
	}

	public String getMsg(){
		return this.text;
	}
	
	public ImageIcon getPicture(){
		return picture;
	}
	
	public void inputMessage(Object o){
		text = (String)o;
	}
	
	private void inputText() {
		text = JOptionPane.showInputDialog("Skriv meddelande");
	}
	
	public void setPicture(){
		new SelectPicture();
	}
	
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
	
	public void setReciver(String recievers){
		this.recievers = recievers;
		
		
	}
	
	public String[] getReciever(){
		String[] reciever = ((String) recievers).split(" ");
		return reciever;
	}
	
	public void setSender(String sender){
		this.sender = sender;
	}
	
	public String getSender(){
		return this.sender;
	}
	public static void main(String[] args){
		Message msg = new Message();
	}
}