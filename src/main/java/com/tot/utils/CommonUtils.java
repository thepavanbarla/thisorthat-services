package com.tot.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CommonUtils {
	
	private static Properties loadApplicationProperties(String resourceFileName) throws IOException {
        Properties configuration = new Properties();
        InputStream inputStream = CommonUtils.class
          .getClassLoader()
          .getResourceAsStream(resourceFileName);
        configuration.load(inputStream);
        inputStream.close();
        return configuration;
    }
	
	public static String getActiveProfile() {
		Properties configuration;
		try {
			configuration = loadApplicationProperties("application.properties");
		} catch (IOException e) {
			return "local";
		}
		return configuration.getProperty("spring.profiles.active");
	}
}
