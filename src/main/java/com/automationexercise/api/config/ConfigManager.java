package com.automationexercise.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigManager {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = ConfigManager.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException("config.properties not found on classpath");
            }
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config.properties", e);
        }
    }

    private ConfigManager() {
    }

    public static String baseUri() {
        return System.getProperty("base.uri", PROPERTIES.getProperty("base.uri"));
    }

    public static int connectionTimeout() {
        return Integer.parseInt(PROPERTIES.getProperty("connection.timeout", "15000"));
    }
}
