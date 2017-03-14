package chatt;

import javax.swing.SwingUtilities;

public class ClientMain {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ClientController cc = new ClientController("127.0.0.1", 3450);
			}
		});
	}
}