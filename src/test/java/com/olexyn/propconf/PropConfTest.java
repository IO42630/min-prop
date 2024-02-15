package com.olexyn.propconf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class PropConfTest {

	private static final String TEST_FILE = "test.properties";

	@Before
    public void setUp() {
		PropConf.clear();
	}



	@Test
    public void testLoadProperties() {
		PropConf.loadProperties(TEST_FILE);
		assertTrue(PropConf.is("booleanProperty"));
	}

	@Test
    public void testIs() {
		PropConf.loadProperties(TEST_FILE);
		assertTrue(PropConf.is("booleanProperty"));
		assertFalse(PropConf.is("nonExistentProperty"));
	}

	@Test
    public void testGetDuration() {
		PropConf.loadProperties(TEST_FILE);
		assertEquals(1000, PropConf.getDuration("durationProperty").toMillis());
		assertEquals(60 * 1000, PropConf.getDuration("minutesProperty").toMillis());
	}

	@Test
    public void testGet() {
		PropConf.loadProperties(TEST_FILE);
		assertEquals("true", PropConf.get("booleanProperty"));
		assertEquals("", PropConf.get("nonExistentProperty"));
	}

	@Test
    public void testGetVarargs() {
		PropConf.loadProperties(TEST_FILE);
		assertEquals("truetrue", PropConf.get("booleanProperty", "booleanProperty"));
		assertEquals("true", PropConf.get("booleanProperty", "nonExistentProperty"));
		assertEquals("true", PropConf.get("nonExistentProperty", "booleanProperty"));
	}

	@Test
	public void testGetSystem() {
		PropConf.loadProperties(TEST_FILE);
		assertEquals("UTF-8", PropConf.get("file.encoding"));
	}

	@Test
	public void testPropException() {
		PropConf.loadProperties(TEST_FILE);
		assertThrows(PropException.class, () -> PropConf.loadProperties("nonExistentFile"));
	}

	@Test
	public void testPeek() {
		PropConf.loadProperties(TEST_FILE);
		assertTrue(PropConf.peek().containsKey("booleanProperty"));
	}

}