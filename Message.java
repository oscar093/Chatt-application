package chatt;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Message implements Serializable{
	private ImageIcon picture;
	private String text;

	public Message() {
		inputText();
		new SelectPicture();
	}

	public String getMsg(){
		return text;
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
	
	private class SelectPicture{
		public SelectPicture(){
			System.out.println("1");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("jpg", "png", "gif");
			JFileChooser filechooser = new JFileChooser();
			System.out.println("2");
//			filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			filechooser.setFileFilter(filter);
			System.out.println("3");
			int returnVal = filechooser.showOpenDialog(null); //<<
			System.out.println("4");
			if(returnVal == JFileChooser.APPROVE_OPTION){
				File file = filechooser.getSelectedFile();
				BufferedImage bi;
				try{
					bi = ImageIO.read(file);
					picture = new ImageIcon(bi);
				}catch(IOException e){}
				
			}
			JOptionPane.showMessageDialog(null, picture);
		}
	}
	
	public static void main(String[] args){
		Message msg = new Message();
	}
}