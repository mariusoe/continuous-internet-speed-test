package de.marius_oe.cist.result;

public interface ResultWriter {
	/**
	 * Call it to signal the end of the measurements
	 */
	void done();

	/**
	 * Stores the given {@link Result}.
	 * 
	 * @param result
	 *            {@link Result} to store
	 */
	void storeResult(Result result);
}
