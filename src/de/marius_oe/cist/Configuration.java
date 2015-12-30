package de.marius_oe.cist;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Provides access to the configuration.
 * 
 * @author Marius Oehler
 * 
 */
public final class Configuration {

	/**
	 * Defines the configuration properties and their default values.
	 */
	public static enum Key {
		DELAY_FIRST_MEASUREMENT("false"),
		LINE_SEPARATOR(System.getProperty("line.separator")),
		MAX_DOWNLOAD_VOLUME("0"),
		MAX_MEASUREMENT_DURATION("10000"),
		MAX_MEASUREMENTS("5"),
		MEASUREMENT_DELAY("5000"),
		OVERWRITE_RESULT_FILE("false"),
		RESULT_FILE("result.csv"),
		RESULT_FORMAT("csv"),
		SHOW_SPEEDLISTENER("true"),
		SPEEDLISTENER_DELAY("1000"),
		TEST_FILE_URL(null),
		VALUE_SEPARATOR(";");

		/**
		 * The default value.
		 */
		private String defaultValue;

		/**
		 * Constructor.
		 * 
		 * @param value
		 *            default value
		 */
		private Key(String value) {
			defaultValue = value;
		}
	}

	/**
	 * The loaded properties.
	 */
	private static Properties properties;

	/**
	 * Gets the property object related to the given key.
	 * 
	 * @param key
	 *            key of the property
	 * @return the property itself
	 */
	public static Object get(Key key) {
		if (properties != null && properties.containsKey(key.toString())) {
			return properties.get(key.toString());
		} else {
			return key.defaultValue;
		}
	}

	/**
	 * Returns the property of the given key as a boolean object.
	 * 
	 * @param key
	 *            key of the property
	 * @return the property represented as a boolean
	 */
	public static boolean getBoolean(Key key) {
		return Boolean.parseBoolean((String) get(key));
	}

	/**
	 * Returns the property of the given key as a character object.
	 * 
	 * @param key
	 *            key of the property
	 * @return the property represented as character
	 */
	public static char getChar(Key key) {
		return (Character) get(key);
	}

	/**
	 * Returns the property of the given key as an integer object.
	 * 
	 * @param key
	 *            key of the property
	 * @return the property represented as integer
	 */
	public static int getInt(Key key) {
		return Integer.parseInt((String) get(key));
	}

	/**
	 * Returns the property of the given key as a long object.
	 * 
	 * @param key
	 *            key of the property
	 * @return the property represented as long
	 */
	public static long getLong(Key key) {
		return Long.parseLong((String) get(key));
	}

	/**
	 * Returns the property of the given key as a string object.
	 * 
	 * @param key
	 *            key of the property
	 * @return the property represented as a string
	 */
	public static String getString(Key key) {
		return (String) get(key);
	}

	/**
	 * Load the specified configuration file.
	 * 
	 * @param configFile
	 *            the configuration file
	 */
	public static void load(String configFile) {
		try {
			InputStream inStream = new FileInputStream(configFile);
			properties = new Properties();
			properties.load(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Hidden constructor.
	 */
	private Configuration() {
	}

}
