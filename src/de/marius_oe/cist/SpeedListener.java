package de.marius_oe.cist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpeedListener extends Thread {

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

	public void start(SpeedTest speedTest) {
		this.speedTest = speedTest;
		running = true;
		start();
	}
}
