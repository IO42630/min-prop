package com.olexyn.propconf;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@UtilityClass
public class PropConf {

	private static final Properties config = new Properties();

	private static final String EMPTY = "";

	private static File resolveFile(String fileName) {
		var urlViaCl = PropConf.class.getClassLoader().getResource(fileName);
		if (urlViaCl != null) {
			var clFile =  new File(urlViaCl.getFile());
			if (clFile.isFile()) {
				return clFile;
			}
		}
		throw new PropException("File not found: " + fileName);
	}

	public static void loadProperties(String fileName) {
		var file = resolveFile(fileName);
		try (var fis = new FileInputStream(file.getAbsolutePath())) {
			config.load(fis);
		} catch (IOException e) {
			throw new PropException("File not found: " + fileName);
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

	public static Duration getDuration(String key) {
		long amount = Long.parseLong(get(key));
		if (key.contains("minutes")) {
			return Duration.ofMinutes(amount);
		}
		return Duration.ofMillis(amount);
	}

	@NonNull
	public static String get(String key) {
		String confProperty = config.getProperty(key);
		if (confProperty != null) {
			return confProperty;
		}
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
