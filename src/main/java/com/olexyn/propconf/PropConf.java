package com.olexyn.propconf;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PropConf {

	private static final Properties config = new Properties();

	public static void loadProperties(String absFileName) {
		try (var fis = new FileInputStream(absFileName)) {
			config.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
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

	public static String get(String key) {
		String confProperty = config.getProperty(key);
		if (confProperty != null) {
			return confProperty;
		}
		return System.getProperty(key);
	}

	public static String get(String... keys) {
		StringBuilder sb = new StringBuilder();
		for (String key : keys) {
			sb.append(get(key));
		}
		return sb.toString();
	}

}
