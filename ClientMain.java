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
//				ClientController cc2 = new ClientController("192.168.1.9", 3450);
//				ClientController cc5 = new ClientController("10.2.7.234", 3450);
				ClientController cc6 = new ClientController("127.0.0.1", 3450);
				ClientController cc7 = new ClientController("127.0.0.1", 3450);
//				ClientController cc8 = new ClientController("127.0.0.1", 3450);				
			}
		});
	}
}