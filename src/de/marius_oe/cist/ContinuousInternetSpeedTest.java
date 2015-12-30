package de.marius_oe.cist;

import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.marius_oe.cist.Configuration.Key;
import de.marius_oe.cist.result.CsvResultWriter;
import de.marius_oe.cist.result.Result;
import de.marius_oe.cist.result.ResultWriter;

/**
 * The main class of the continuous-internet-speed-test.
 * 
 * @author Marius Oehler
 *
 */
public class ContinuousInternetSpeedTest {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ContinuousInternetSpeedTest.class);

	/**
	 * Counter of measurements.
	 */
	private static int measurementCounter = 0;

	/**
	 * The used {@link ResultWriter}.
	 */
	private static ResultWriter resultWriter;

	/**
	 * The main method.
	 * 
	 * @param args
	 *            program arguments
	 * @throws MalformedURLException
	 *             will be thrown if the URL of the testfile is not valid.
	 */
	public static void main(String[] args) throws MalformedURLException {
		if (args.length <= 0) {
			Configuration.load("cist-settings.ini");
		}

		if (Configuration.getString(Key.RESULT_FORMAT).equals("csv")) {
			resultWriter = new CsvResultWriter();
		} else {
			throw new IllegalArgumentException("Unsupportet result format.");
		}

		// For better readability.
		int maxMeasurments = Configuration.getInt(Key.MAX_MEASUREMENTS);
		long maxDuration = Configuration.getInt(Key.MAX_MEASUREMENT_DURATION);
		long maxVolume = Configuration.getInt(Key.MAX_DOWNLOAD_VOLUME);

		logger.info("Starting Continious-Internet-Speed-Test");
		logger.info("> Max number measurements: {}", (maxMeasurments == 0) ? "unlimited" : maxMeasurments);
		logger.info("> Max measurement duration: {} ms", (maxDuration == 0) ? "unlimited" : maxDuration);
		logger.info("> Max measurement volume: {}",
				(maxVolume == 0) ? "unlimited" : Util.humanReadableByteCount(maxVolume));

		long averageBytesPerSecond = 0;

		while (true) {
			if (measurementCounter > 0 || Configuration.getBoolean(Key.DELAY_FIRST_MEASUREMENT)) {
				logger.info("Next speed-test in {}",
						Util.humanReadableDuration(Configuration.getInt(Key.MEASUREMENT_DELAY)));

				try {
					Thread.sleep(Configuration.getInt(Key.MEASUREMENT_DELAY));
				} catch (InterruptedException e) {
				}
			}
			logger.info("Starting speed-test");

			SpeedTest speedTest = new SpeedTest(Configuration.getString(Key.TEST_FILE_URL));

			if (Configuration.getBoolean(Key.SHOW_SPEEDLISTENER)) {
				SpeedListener speedListener = new SpeedListener(Configuration.getLong(Key.SPEEDLISTENER_DELAY));
				speedTest.setSpeedListener(speedListener);
			}

			// executes the speed-test
			Result result = speedTest.execute();

			resultWriter.storeResult(result);

			long measurementDuration = result.getEndTime() - result.getStartTime();
			averageBytesPerSecond += result.getBytesTransfered() / (measurementDuration / 1000);

			logger.info(result.toString());

			if (maxMeasurments > 0 && ++measurementCounter >= maxMeasurments) {
				break;
			}
		}

		resultWriter.done();

		logger.info("Continious-Internet-Speed-Test has sucessfully ended");
		logger.info("Average speed of {} measurements: {}/sec", measurementCounter,
				Util.humanReadableByteCount(averageBytesPerSecond / measurementCounter));
	}
}
