package chatt;

import javax.swing.ImageIcon;
import java.io.Serializable;
import javax.swing.*;

public class Message implements Serializable{
	private ImageIcon picture;
	private String text;
	private JFileChooser filechooser;

	public Message() {
		inputText();
		inputPicture();
	}

	public String getMsg(){
		return text;
	}
	
	public ImageIcon getPicture(){
		return picture;
	}
	
	private void inputText() {
		text = JOptionPane.showInputDialog("Skriv meddelande");
	}

	private void inputPicture() {
		//filechooser?
	}
}