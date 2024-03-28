package com.olexyn.propconf;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@UtilityClass
public class PropConf {

	private static final String FILE_NOT_FOUND = "File not found: ";

	private static final Properties config = new Properties();

	private static final String EMPTY = "";

	/**
	 * When
	 *
	 * @param fileName
	 * @return
	 */
	private static InputStream resolveIs(String fileName) {
		var is = PropConf.class.getClassLoader().getResourceAsStream(fileName);
		if (is != null) {
			return is;
		}
		throw new PropException(FILE_NOT_FOUND + fileName);
	}



	public static void load(String fileName) {
		try (var fis = resolveIs(fileName)) {
			config.load(fis);
		} catch (IOException e) {
			throw new PropException(FILE_NOT_FOUND + fileName);
		}
	}


	public static void clear() {
		config.clear();
	}

	public static Map<String, String> peek() {
		return config.stringPropertyNames().stream()
			.collect(Collectors.toMap(k -> k, config::getProperty));
	}

	public static boolean is(String key) {
		return Boolean.parseBoolean(get(key));
	}

	public static boolean getBool(String key) {
		return Boolean.parseBoolean(get(key));
	}

	public static int getInt(String key) {
		return Integer.parseInt(get(key));
	}

	public static long getLong(String key) {
		return Long.parseLong(get(key));
	}

	public static boolean isNot(String key) {
		return !is(key);
	}

	public static Path getPath(String key) {
		return Path.of(get(key));
	}

	public static Duration getDuration(String key) {
		long amount = Long.parseLong(get(key));
		if (key.contains("days")) {
			return Duration.ofDays(amount);
		}
		if (key.contains("hours")) {
			return Duration.ofHours(amount);
		}
		if (key.contains("minutes")) {
			return Duration.ofMinutes(amount);
		}
		if (key.contains("seconds")) {
			return Duration.ofSeconds(amount);
		}
		return Duration.ofMillis(amount);
	}

	@NonNull
	public static String get(String key) {
		String confProperty = config.getProperty(key);
		if (confProperty != null) { return confProperty; }
		if (System.getProperty(key) != null) {
			return System.getProperty(key);
		}
		return EMPTY;
	}

	@NonNull
	public static String get(String... keys) {
		StringBuilder sb = new StringBuilder();
		for (String key : keys) {
			sb.append(get(key));
		}
		return sb.toString();
	}

}
