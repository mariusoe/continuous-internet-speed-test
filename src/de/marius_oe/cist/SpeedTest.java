package de.marius_oe.cist;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.marius_oe.cist.Configuration.Key;
import de.marius_oe.cist.result.Result;

public class SpeedTest {

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

	private static final Logger logger = LoggerFactory.getLogger(SpeedTest.class);

	private boolean done = false;
	private boolean maxDurationReached = false;
	private SpeedListener speedListener;
	private URL testFileUrl;
	private TimeoutHandler timeoutHandler;

	private long totalBytesRead = 0;

	public SpeedTest(URL testFileUrl) {
		if (testFileUrl == null) {
			throw new NullPointerException("testFileUrl can not be NULL");
		}
		this.testFileUrl = testFileUrl;
	}

	public Result execute() {
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

	public long getTotalBytesRead() {
		return totalBytesRead;
	}

	public boolean isDone() {
		return done;
	}

	public void setSpeedListener(SpeedListener speedListener) {
		this.speedListener = speedListener;
	}
}
