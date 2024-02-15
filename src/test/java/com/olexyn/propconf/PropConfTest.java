package com.olexyn.propconf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PropConfTest {

	private static final String TEST_FILE = "test.properties";

	@Before
	void setUp() {
		Properties properties = new Properties();
		properties.setProperty("booleanProperty", "true");
		properties.setProperty("durationProperty", "1000");
		properties.setProperty("minutesProperty", "30");
		try (FileOutputStream fos = new FileOutputStream(TEST_FILE)) {
			properties.store(fos, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testLoadProperties() {
		PropConf.loadProperties(TEST_FILE);
		assertTrue(PropConf.is("booleanProperty"));
	}

	@Test
	void testIs() {
		PropConf.loadProperties(TEST_FILE);
		assertTrue(PropConf.is("booleanProperty"));
		assertFalse(PropConf.is("nonExistentProperty"));
	}

	@Test
	void testGetDuration() {
		PropConf.loadProperties(TEST_FILE);
		assertEquals(1000, PropConf.getDuration("millisProperty").toMillis());
		assertEquals(60 * 1000, PropConf.getDuration("minutesProperty").toMillis());
	}

	@Test
	void testGet() {
		PropConf.loadProperties(TEST_FILE);
		assertEquals("true", PropConf.get("booleanProperty"));
		assertNull(PropConf.get("nonExistentProperty"));
	}

	@Test
	void testGetVarargs() {
		PropConf.loadProperties(TEST_FILE);
		assertEquals("truetrue", PropConf.get("booleanProperty", "booleanProperty"));
		assertEquals("truetrue", PropConf.get("booleanProperty", "nonExistentProperty"));
	}

}