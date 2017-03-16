package chatt;

import javax.swing.SwingUtilities;

/**
 * A class for starting a client
 * @author Group 2
 *
 */
public class ClientMain {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
//				ClientController cc1 = new ClientController("10.2.26.226", 3450);
				ClientController cc2 = new ClientController("127.0.0.1", 3450);
//				ClientController cc3 = new ClientController("127.0.0.1", 3450);
			}
		});
	}
}