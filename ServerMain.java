package chatt;

import java.io.IOException;

import javax.swing.SwingUtilities;
/**
 * A class for starting the server
 * @author Group 2
 *
 */
public class ServerMain {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try{
				ServerController sc = new ServerController(3450);
					} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
