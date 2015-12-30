package de.marius_oe.cist;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.marius_oe.cist.Configuration.Key;
import de.marius_oe.cist.result.Result;

/**
 * The implementation of the speed-test.
 * 
 * @author Marius Oehler
 *
 */
public class SpeedTest {

	/**
	 * Handler that handles the timeout of measurements.
	 */
	private class TimeoutHandler extends Thread {
		@Override
		public void run() {
			if (Configuration.getLong(Key.MAX_MEASUREMENT_DURATION) <= 0) {
				return;
			}
			try {
				Thread.sleep(Configuration.getLong(Key.MAX_MEASUREMENT_DURATION));
			} catch (InterruptedException e) {
				return;
			}
			maxDurationReached = true;
		}
	}

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(SpeedTest.class);

	private boolean done = false;

	private boolean maxDurationReached = false;

	private SpeedListener speedListener;

	private String testFileUrlPattern;

	private TimeoutHandler timeoutHandler;

	private long totalBytesRead = 0;

	/**
	 * Constructor.
	 * 
	 * @param testFileUrlPattern
	 *            A pattern which represents the URL of the test file.
	 */
	public SpeedTest(String testFileUrlPattern) {
		if (testFileUrlPattern == null) {
			throw new NullPointerException("testFileUrlPattern can not be NULL");
		}
		this.testFileUrlPattern = testFileUrlPattern;
	}

	/**
	 * Starts the speed-test.
	 * 
	 * @return Returns the test result.
	 */
	public Result execute() {
		URL testFileUrl = generateTestFileUrl();
		logger.info("Using test-file: {}", testFileUrl);

		if (speedListener != null) {
			speedListener.start(this);
		}

		timeoutHandler = new TimeoutHandler();
		timeoutHandler.start();

		boolean downloadLimitReached = false;

		Result result = new Result();
		result.setStartTime(System.currentTimeMillis());

		try {
			URLConnection conn = testFileUrl.openConnection();
			InputStream inStream = conn.getInputStream();

			long downloadLimit = Configuration.getLong(Key.MAX_DOWNLOAD_VOLUME);

			byte[] buffer = new byte[1024];
			long bytesRead;
			while (!maxDurationReached && (bytesRead = inStream.read(buffer)) != -1) {
				totalBytesRead += bytesRead;
				if (downloadLimit > 0 && totalBytesRead >= downloadLimit) {
					downloadLimitReached = true;
					break;
				}
			}

			result.setBytesTransfered(totalBytesRead);

			inStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			done = true;
		}

		result.setEndTime(System.currentTimeMillis());

		if (speedListener != null) {
			speedListener.done();
		}

		if (maxDurationReached) {
			logger.info("Measurement stopped, reached maximum execution duration");
		} else if (downloadLimitReached) {
			logger.info("Measurement stopped, reached maximum download volume");
		} else {
			logger.info("Measurement ended, reached end of test-file");
		}

		return result;
	}

	/**
	 * Generates the {@link URL} of the test-file. Here, placeholder will be
	 * replaced.
	 * 
	 * @return the {@link URL} of the test-file
	 */
	private URL generateTestFileUrl() {
		String url = testFileUrlPattern;

		// replace placeholder
		url = url.replace("{CURRENTTIMEMILLIS}", String.valueOf(System.currentTimeMillis()));

		url = url.trim();
		try {
			if (url.isEmpty()) {
				throw new RuntimeException("URL of testfile cannot be empty.");
			}
			return new URL(url);
		} catch (RuntimeException e) {
			logger.error("Please specify a valid URL for the test-file.", e);
			throw new IllegalArgumentException(e);
		} catch (MalformedURLException e) {
			logger.error("The given URL is not valid", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the total amount of read bytes.
	 * 
	 * @return amount of read bytes
	 */
	public long getTotalBytesRead() {
		return totalBytesRead;
	}

	/**
	 * Returns the status of the speed-test.
	 * 
	 * @return Returns whether the speed-test is done.
	 */
	public boolean isDone() {
		return done;
	}

	/**
	 * Sets a {@link SpeedListener} to handle the progress of the speed-test.
	 * 
	 * @param speedListener
	 *            {@link SpeedListener} to use
	 */
	public void setSpeedListener(SpeedListener speedListener) {
		this.speedListener = speedListener;
	}
}
