/**
 * SYSC3303 Project
 * Group 1
 * @version 3.0
 */
package sysc_3303_project.common.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * A resource manager for properties information. Singleton style
 * @author Liam
 *
 */
public class ResourceManager {
	
	/**
	 * A static instance of the class
	 */
	private static ResourceManager resourceManager;
	
	/**
	 * The properties wrapper
	 */
	private Properties prop = null;
	
	/**
	 * A path that has the properties file
	 */
	private String path = "resources/config.properties";
	
	/**
	 * Constructor
	 */
	private ResourceManager() {
		prop = buildProperties();
	}
	
	/**
	 * Returns a property
	 * @param s		String, key to get from
	 * @return
	 */
	public String get(String s) {
		return prop.getProperty(s);
		
	}
	
	/**
	 * Returns a property as an int
	 * @param s
	 * @return
	 */
	public int getInt(String s) {
		return Integer.parseInt(get(s));
	}

	/**
	 * Builds a properties file from the static path
	 * @return	Properties, a properties file for configuration details
	 */
	private Properties buildProperties() {
        try (InputStream input = new FileInputStream(path)) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);
            
            return prop;

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return null;
	}
	
	/**
	 * For testing purposes. Changes path file to use
	 * @param s	String, path tile to use
	 */
	public void changePath(String s) {
		prop = null;
		path = s;
	}
	
	/**
	 * returns the singular instance of the projects ResourcceManager
	 * @return		ResourceManager, the singleton object
	 */
	public static ResourceManager get() {
		if (resourceManager == null) {
			resourceManager = new ResourceManager();
		}
		return resourceManager;
	}

}
