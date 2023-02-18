/**
 * 
 */
package logging;

import java.time.LocalDateTime;

/**
 * @author apope
 *
 */
public class Logger {
	
	private static Logger singleton = null;
	
	private Logger() {
		
	}
	
	public static Logger getLogger() {
		if (singleton == null) {
			singleton = new Logger();
		}
		return singleton;
	}
	
	private void log(String className, String logLevel, String message) {
		LocalDateTime time = LocalDateTime.now();
		System.out.println(String.format("[Time: %s] [%s] [Class: %s] [%s] %s", time.toString(), Thread.currentThread().getName(), className, logLevel, message));
	}
	
	public void logNotification(String className, String message) {
		log(className, "Notification", message);
	}

	public void logError(String className, String message) {
		log(className, "Error", message);
	}

}
