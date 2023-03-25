/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
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
	
	private Level level = Level.DEBUG;
	
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
	 * Logs a debug to the console.
	 * @param className the name of the class displaying a log
	 * @param message the message to display
	 */
	public void logDebug(String className, String message) {
		if(this.level.getLevel() >= Level.DEBUG.getLevel())
			log(className, "Debug", message);
	}

	
	/**
	 * Logs a notification to the console.
	 * @param className the name of the class displaying a log
	 * @param message the message to display
	 */
	public void logNotification(String className, String message) {
		if(this.level.getLevel() >= Level.NOTIFICATION.getLevel())
			log(className, "Notification", message);
	}

	/**
	 * Logs an error to the console.
	 * @param className the name of the class displaying a log
	 * @param message the message to display
	 */
	public void logError(String className, String message) {
		if(this.level.getLevel() >= Level.ERROR.getLevel())
			log(className, "Error", message);
	}
	
	/**
	 * Enum for log levels
	 * @author Liam
	 *
	 */
	private enum Level {
		ERROR(0),			// only log errors
		NOTIFICATION(1),	//only log notifications and above
		DEBUG(2);			//only log debugs and above
		
		int rank;
		
		private Level(int i) {
			this.rank = i;
		}
		
		public int getLevel() {
			return rank;
		}
	};

}
