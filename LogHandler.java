package chatt;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.ImageIcon;

public class LogHandler {

	private Logger log;
	private FileHandler fh;

	public LogHandler() throws SecurityException, IOException {
		log = Logger.getLogger("Serverlog");
		fh = new FileHandler("serverlog.log", true);
		log.addHandler(fh);
		fh.setFormatter(new SimpleFormatter());
		// log.setUseParentHandlers(false);

	}

	public void logMessage(Message message, String sender) {

		log.info(sender + " sent: ");
		log.info(message.getMsg());

	}

	public void logServerMessage(String string) {

		log.warning(string);

	}

	public void logError(Exception e) {
		log.severe("" + e);
	}
}
