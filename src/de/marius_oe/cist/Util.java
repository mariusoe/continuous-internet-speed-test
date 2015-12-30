package de.marius_oe.cist;

/**
 * Utility class.
 * 
 * @author Marius Oehler
 *
 */
public final class Util {

	/**
	 * Converts the amount of given bytes into a human readable format.
	 *
	 * @param bytes
	 *            bytes to format
	 * @return human readable string
	 */
	public static String humanReadableByteCount(long bytes) {
		int base = 1024;
		if (bytes < base) {
			return bytes + " B";
		}
		int exp = (int) (Math.log(bytes) / Math.log(base));
		char pre = ("KMGTPE").charAt(exp - 1);
		return String.format("%.1f %sB", bytes / Math.pow(base, exp), pre);
	}

	/**
	 * Converts the amount of milliseconds in a human readable format.
	 *
	 * @param milliseconds
	 *            milliseconds to format
	 * @return human readable string
	 */
	public static String humanReadableDuration(long milliseconds) {
		long totalSeconds = milliseconds / 1000;
		long hours = totalSeconds / 3600;
		long minutes = (totalSeconds - hours * 3600) / 60;
		long seconds = totalSeconds % 60;
		return String.format("%s hour(s) %s minute(s) %s second(s)", hours, minutes, seconds);
	}

	/**
	 * Hidden constructor.
	 */
	private Util() {
	}
}
