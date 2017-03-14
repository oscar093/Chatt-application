package chatt;

import javax.swing.SwingUtilities;

public class ServerMain {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ServerController sc = new ServerController(3450);
			}
		});
	}
}
