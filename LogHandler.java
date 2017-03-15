package chatt;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Logs activities in a text file.
 * @author Alex
 *
 */

public class LogHandler {

	private Logger log;
	private FileHandler fh;

	/**
	 * Creates a logger
	 * @throws SecurityException
	 * @throws IOException
	 */
	public LogHandler() throws SecurityException, IOException {
		log = Logger.getLogger("Serverlog");
		fh = new FileHandler("serverlog.log", true);
		log.addHandler(fh);
		fh.setFormatter(new SimpleFormatter());
		// log.setUseParentHandlers(false);

	}
	
	/**
	 * Logs a message with the sender´s name in a logfile
	 * @param message the sent message
	 * @param sender the sender
	 */

	public void logMessage(Message message, String sender) {

		log.info(sender + " sent: ");
		log.info(message.getMsg());

	}
	/**
	 * Logs user activity
	 * 
	 * @param string
	 *            the string to log
	 */

	public void logServerMessage(String string) {

		log.info(string);

	}
	/**
	 * Method for logging errors.
	 * @param e the error
	 */
	public void logError(Exception e) {
		log.severe("" + e);
	}
}
