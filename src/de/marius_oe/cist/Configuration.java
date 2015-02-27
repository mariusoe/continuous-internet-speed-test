package de.marius_oe.cist;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Configuration {

	public static enum Key {
		DELAY_FIRST_MEASUREMENT("false"), LINE_SEPARATOR(System.getProperty("line.separator")), MAX_DOWNLOAD_VOLUME("0"), MAX_MEASUREMENT_DURATION("10000"), MAX_MEASUREMENTS(
				"5"), MEASUREMENT_DELAY("5000"), OVERWRITE_RESULT_FILE("false"), RESULT_FILE("result.csv"), RESULT_FORMAT("csv"), SHOW_SPEEDLISTENER("true"), SPEEDLISTENER_DELAY(
				"1000"), TEST_FILE_URL(null), VALUE_SEPARATOR(";");

		private String defaultValue;

		private Key(String value) {
			defaultValue = value;
		}
	}

	private static Properties properties;

	public static Object get(Key key) {
		if (properties != null && properties.containsKey(key.toString())) {
			return properties.get(key.toString());
		} else {
			return key.defaultValue;
		}
	}

	public static boolean getBoolean(Key key) {
		return Boolean.parseBoolean((String) get(key));
	}

	public static char getChar(Key key) {
		return (Character) get(key);
	}

	public static int getInt(Key key) {
		return Integer.parseInt((String) get(key));
	}

	public static long getLong(Key key) {
		return Long.parseLong((String) get(key));
	}

	public static String getString(Key key) {
		return (String) get(key);
	}

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
