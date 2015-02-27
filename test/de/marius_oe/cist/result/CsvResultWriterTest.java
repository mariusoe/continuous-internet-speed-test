package de.marius_oe.cist.result;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

public class CsvResultWriterTest {

	private CsvResultWriter csvWriter;
	private StringWriter stringWriter;

	@Before
	public void setUp() {
		stringWriter = new StringWriter();
		csvWriter = new CsvResultWriter(stringWriter);
	}

	@Test
	public void test_constructor_01() {
		String expected = "timestamp;duration;bytes_transfered;average_speed_kbs";
		assertEquals(expected, stringWriter.getBuffer().toString());
	}

	@Test
	public void test_storeResult_01() {
		Result testResult = new Result();
		testResult.setStartTime(1000);
		testResult.setEndTime(5000);
		testResult.setBytesTransfered(8000);

		csvWriter.storeResult(testResult);

		String expected = "timestamp;duration;bytes_transfered;average_speed_kbs" + System.getProperty("line.separator") + "1000;4000;8000;2";
		assertEquals(expected, stringWriter.getBuffer().toString());
	}
}
