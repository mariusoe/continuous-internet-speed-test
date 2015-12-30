package de.marius_oe.cist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener to track the progress of a speed-test
 * 
 * @author Marius Oehler
 *
 */
public class SpeedListener extends Thread {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(SpeedListener.class);

	private long interval;
	
	private boolean running;
	
	private SpeedTest speedTest;

	/**
	 *
	 * @param interval
	 *            time between samples in milliseconds
	 */
	public SpeedListener(long interval) {
		this.interval = interval;
	}

	/**
	 * Will be called when the test is done.
	 */
	public void done() {
		running = false;
		interrupt();
	}

	@Override
	public void run() {
		long lastBytes = 0;
		while (running) {
			long currentBytes = speedTest.getTotalBytesRead();
			long delta = currentBytes - lastBytes;
			long bytesPerSecond = (long) (delta / (interval / 1000D));
			logger.info("{}/sec", Util.humanReadableByteCount(bytesPerSecond));

			lastBytes = currentBytes;
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Will be called when the speed-test is starting.
	 * 
	 * @param speedTest
	 *            the speed-test that is observed
	 */
	public void start(SpeedTest speedTest) {
		this.speedTest = speedTest;
		running = true;
		start();
	}
}
