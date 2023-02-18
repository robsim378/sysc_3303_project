/**
 * 
 */
package logging;

import java.time.LocalDateTime;

/**
 * @author Andrei Popescu
 * A simple logger class to keep logs in a consistent format.
 * Does not ensure well-ordering of logs among different threads.
 * Implemented as a singleton.
 */
public class Logger {
	
	private static Logger singleton = null;
	
	/**
	 * Creates a new Logger.
	 */
	private Logger() {
		
	}
	
	/**
	 * Gets the Logger singleton object.
	 * @return the Logger
	 */
	public static Logger getLogger() {
		if (singleton == null) {
			singleton = new Logger();
		}
		return singleton;
	}
	
	/**
	 * Logs a message to the console with a specified log level.
	 * @param className the name of the class displaying a log
	 * @param logLevel the log level of the message to be displayed
	 * @param message the message to display
	 */
	private void log(String className, String logLevel, String message) {
		LocalDateTime time = LocalDateTime.now();
		System.out.println(String.format("[Time: %s] [%s] [Class: %s] [%s] %s", time.toString(), Thread.currentThread().getName(), className, logLevel, message));
	}
	
	/**
	 * Logs a notification to the console.
	 * @param className the name of the class displaying a log
	 * @param message the message to display
	 */
	public void logNotification(String className, String message) {
		log(className, "Notification", message);
	}

	/**
	 * Logs an error to the console.
	 * @param className the name of the class displaying a log
	 * @param message the message to display
	 */
	public void logError(String className, String message) {
		log(className, "Error", message);
	}

}
