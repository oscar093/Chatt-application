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

public class Message implements Serializable {
	private ImageIcon picture;
	private String text;
	private String recievers, sender;
	private ArrayList<String> recievedList = new ArrayList<String>();
	private int id;
	private boolean isSent = false;

	private Random rand = new Random();

	public Message() {
		id = rand.nextInt(10000);
	}

	public boolean isSent() {
		return isSent;
	}

	public void setIsSent(boolean bool) {
		isSent = bool;
	}

	public int getID() {
		return id;
	}

	public void generateNewID() {
		this.id = rand.nextInt(10000);
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getMsg() {
		return this.text;
	}

	public ImageIcon getPicture() {
		return picture;
	}

	public void inputMessage(Object o) {
		text = (String) o;
	}

	private void inputText() {
		text = JOptionPane.showInputDialog("Skriv meddelande");
	}

	public void setPicture() {
		new SelectPicture();
	}

	private class SelectPicture {
		public SelectPicture() {
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

	public String getRecievedBy(int element) {
		if (recievedList.size() > element) {
			return recievedList.get(element);
		} else {
			return "NoOneShouldHaveAIDLikeThisOne"; // Kan inte skicka null så
													// måste göra såhar.
		}
	}

	public boolean isRecievedBy(String username) {
		boolean bool = false;
		for (String strRec : recievedList) {
			if (strRec.contentEquals(username)) {
				bool = true;
			}
		}

		return bool;
	}

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

	public void setRecievedBy(String recievedBy) {
		recievedList.add(recievedBy);
	}

	public void setReciver(String recievers) {
		this.recievers = recievers;
	}

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

	public String[] getReciever() {
		String[] reciever = { "" };
		if (recievers != null) {
			reciever = ((String) recievers).split(" ");
			return reciever;
		} else {
			return reciever;
		}

	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSender() {
		return this.sender;
	}

	public static void main(String[] args) {
		Message msg = new Message();
	}
}