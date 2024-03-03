package com.olexyn.propconf;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@UtilityClass
public class PropConf {

	private static final String FILE_NOT_FOUND = "File not found: ";

	private static final Properties config = new Properties();
	private static final Properties events = new Properties();

	private static final String EMPTY = "";

	private static File resolveFile(String fileName) {
		var urlViaCl = PropConf.class.getClassLoader().getResource(fileName);
		if (urlViaCl != null) {
			var clFile =  new File(urlViaCl.getFile());
			if (clFile.isFile()) {
				return clFile;
			}
		}
		throw new PropException(FILE_NOT_FOUND + fileName);
	}

	public static void loadConfig(String fileName) {
		load(config, fileName);
	}

	public static void loadEvents(String fileName) {
		load(events, fileName);
	}


	private static void load(Properties target, String fileName) {
		var file = resolveFile(fileName);
		try (var fis = new FileInputStream(file.getAbsolutePath())) {
			target.load(fis);
		} catch (IOException e) {
			throw new PropException(FILE_NOT_FOUND + fileName);
		}
	}


	public static void saveEvents( String fileName) {
		var file = resolveFile(fileName);
		try (var fos = new FileOutputStream(file)) {
			events.store(fos, EMPTY);
			fos.flush();
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
		String eventProperty = events.getProperty(key);
		if (eventProperty != null) { return eventProperty; }
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
