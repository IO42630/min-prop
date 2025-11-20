package com.olexyn.propconf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PropConfTest {

	private static final String TEST_FILE = "test.properties";

	@BeforeEach
    public void setUp() {
		PropConf.clear();
	}



	@Test
    public void testLoadProperties() {
        PropConf.load(TEST_FILE);
		assertTrue(PropConf.is("booleanProperty"));
	}

	@Test
    public void testIs() {
        PropConf.load(TEST_FILE);
		assertTrue(PropConf.is("booleanProperty"));
		assertFalse(PropConf.is("nonExistentProperty"));
	}

	@Test
    public void testGetDuration() {
        PropConf.load(TEST_FILE);
		assertEquals(1000, PropConf.getDuration("durationProperty").toMillis());
		assertEquals(60 * 1000, PropConf.getDuration("minutesProperty").toMillis());
	}

	@Test
    public void testGet() {
        PropConf.load(TEST_FILE);
		assertEquals("true", PropConf.get("booleanProperty"));
		assertEquals("", PropConf.get("nonExistentProperty"));
	}

	@Test
    public void testGetVarargs() {
        PropConf.load(TEST_FILE);
		assertEquals("truetrue", PropConf.get("booleanProperty", "booleanProperty"));
		assertEquals("true", PropConf.get("booleanProperty", "nonExistentProperty"));
		assertEquals("true", PropConf.get("nonExistentProperty", "booleanProperty"));
	}

	@Test
	public void testGetSystem() {
        PropConf.load(TEST_FILE);
		assertEquals("UTF-8", PropConf.get("file.encoding"));
	}

	@Test
	public void testPropException() {
        PropConf.load(TEST_FILE);
        assertThrows(PropException.class, () -> PropConf.load("nonExistentFile"));
	}

	@Test
	public void testPeek() {
        PropConf.load(TEST_FILE);
		assertTrue(PropConf.peek().containsKey("booleanProperty"));
	}

}
