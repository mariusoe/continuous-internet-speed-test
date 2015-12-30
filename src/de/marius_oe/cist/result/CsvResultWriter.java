package de.marius_oe.cist.result;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.marius_oe.cist.Configuration;
import de.marius_oe.cist.Configuration.Key;

/**
 * Writer that writes the results as a csv list.
 * 
 * @author Marius Oehler
 *
 */
public class CsvResultWriter implements ResultWriter {

	/** The output writer. */
	private Writer outWriter;

	/**
	 * Constructor.
	 */
	public CsvResultWriter() {
		this(null);
	}

	/**
	 * Constructor. The CSV data will be written in the given writer.
	 *
	 * @param writer
	 *            writer for the CSV data
	 */
	public CsvResultWriter(Writer writer) {
		if (writer == null) {
			outWriter = getDefaultWriter();
		} else {
			outWriter = writer;
		}

		writeHeader();
	}

	/**
	 * Closes the underlying writer.
	 */
	public void done() {
		try {
			outWriter.flush();
			outWriter.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the default writer, if no writer was given.
	 *
	 * @return
	 */
	private Writer getDefaultWriter() {
		try {
			File resultFile = new File(Configuration.getString(Key.RESULT_FILE));

			if (!Configuration.getBoolean(Key.OVERWRITE_RESULT_FILE) && resultFile.exists()) {
				String newName;

				SimpleDateFormat dateTimeFile = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
				String date = dateTimeFile.format(new Date());

				if (Configuration.getString(Key.RESULT_FILE).indexOf(".") != -1) {
					int dotIndex = Configuration.getString(Key.RESULT_FILE).lastIndexOf(".");

					String file = Configuration.getString(Key.RESULT_FILE);
					newName = file.substring(0, dotIndex) + "_" + date + file.substring(dotIndex);
				} else {
					newName = Configuration.getString(Key.RESULT_FILE) + date;
				}

				resultFile = new File(newName);
			}

			return new FileWriter(resultFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Stores the given result as a line in the CSV writer.
	 */
	public void storeResult(Result result) {
		long startTime = result.getStartTime();
		long duration = result.getEndTime() - startTime;
		long bytesTransfered = result.getBytesTransfered();
		long bytesPerSecond = (duration > 0) ? ((bytesTransfered / 1000) / (duration / 1000)) : (-1);

		writeLine(new Object[] { startTime, duration, bytesTransfered, bytesPerSecond });
	}

	/**
	 * Writes the header of the CSV stream.
	 */
	private void writeHeader() {
		String[] headerFields = new String[] { "timestamp", "duration", "bytes_transfered", "average_speed_kbs" };
		writeLine(headerFields, false);
	}

	/**
	 * Writes the given objects into the CSV writer and appends the 'new line'
	 * character.
	 *
	 * @param objects
	 *            objects to write into the writer.
	 */
	private void writeLine(Object[] objects) {
		writeLine(objects, true);
	}

	/**
	 * Writes the given objects into the CSV writer. Appends a 'new line'
	 * character if <code>newLine</code> is true.
	 *
	 * @param objects
	 *            objects to write into the writer
	 * @param newLine
	 *            specifies whether a 'new line' character should be set before
	 *            the line.
	 */
	private void writeLine(Object[] objects, boolean newLine) {
		try {
			if (newLine) {
				outWriter.write(Configuration.getString(Key.LINE_SEPARATOR));
			}
			boolean isFirst = true;
			for (Object obj : objects) {
				if (!isFirst) {

					outWriter.write(Configuration.getString(Key.VALUE_SEPARATOR));

				} else {
					isFirst = false;
				}
				outWriter.write(obj.toString());
			}
			outWriter.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
