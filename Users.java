package chatt;

import java.util.ArrayList;

/**
 * Class for using synchronized methods to add users to a list and returning
 * users.
 * 
 * @author Grupp 2
 *
 */

public class Users {

	private ArrayList<Connect> users = new ArrayList<Connect>();

	public synchronized boolean containsKey(String key) {
		for (Connect c : users) {
			if (c.getUsername().contentEquals(key)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds users to arraylist
	 * 
	 * @param e the user
	 */

	public synchronized void addUser(Connect e) {
		this.users.add(e);
	}

	/**
	 * Returns users
	 * 
	 * @return the array
	 */
	public synchronized Connect[] getUsers() {
		Connect[] arr = new Connect[users.size()];
		users.toArray(arr);
		return arr;
	}
}
