package de.marius_oe.cist;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UtilTest {

	@Test
	public void test_humanReadableDuration_01() {
		String hrDuration = Util.humanReadableDuration(1000);
		assertEquals("0 hour(s) 0 minute(s) 1 second(s)", hrDuration);
	}
	
	@Test
	public void test_humanReadableDuration_02() {
		String hrDuration = Util.humanReadableDuration(60000);
		assertEquals("0 hour(s) 1 minute(s) 0 second(s)", hrDuration);
	}
	
	@Test
	public void test_humanReadableDuration_03() {
		String hrDuration = Util.humanReadableDuration(3600000);
		assertEquals("1 hour(s) 0 minute(s) 0 second(s)", hrDuration);
	}

	@Test
	public void test_humanReadableDuration_04() {
		String hrDuration = Util.humanReadableDuration(3661000);
		assertEquals("1 hour(s) 1 minute(s) 1 second(s)", hrDuration);
	}
}
