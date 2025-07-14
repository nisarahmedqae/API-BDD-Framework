package com.nahmed.utils;

import com.nahmed.constants.FrameworkConstants;
import com.nahmed.enums.ConfigProperties;
import com.nahmed.exceptions.PropertyFileUsageException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public final class PropertyUtils {

    private PropertyUtils() {

    }

    private static Properties prop = new Properties();
    private static final Map<String, String> CONFIGMAP = new HashMap<>();

    static {
        try (FileInputStream fis = new FileInputStream(FrameworkConstants.getConfigFilePath())) {
            prop.load(fis);

            for (Map.Entry<Object, Object> entry : prop.entrySet()) {
                CONFIGMAP.put(entry.getKey().toString().toLowerCase(), entry.getValue().toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static String getValue(ConfigProperties key) {
        if (Objects.isNull(key) || Objects.isNull(CONFIGMAP.get(key.toString().toLowerCase()))) {
            throw new PropertyFileUsageException(
                    "Property name " + key + " is not found. Please check config.properties");
        }
        return CONFIGMAP.get(key.toString().toLowerCase());
    }

    public static String getValue(String key) {
        if (Objects.isNull(key) || Objects.isNull(CONFIGMAP.get(key.toLowerCase()))) {
            throw new PropertyFileUsageException(
                    "Property name " + key + " is not found. Please check config.properties");
        }
        return CONFIGMAP.get(key.toLowerCase());
    }

    // Method to read a data_store property value
    public static String getProperty(String key) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(FrameworkConstants.getDataStoreFilePath())) {
            properties.load(fis);
            return properties.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to update a data_store property value
    public static void setProperty(String key, String value) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(FrameworkConstants.getDataStoreFilePath())) {
            properties.load(fis); // Load existing properties
        } catch (IOException e) {
            System.out.println("Could not load existing properties: " + e.getMessage());
        }

        properties.setProperty(key, value); // Set/update the property

        try (FileOutputStream fos = new FileOutputStream(FrameworkConstants.getDataStoreFilePath())) {
            properties.store(fos, "Updated during testing"); // Store back to file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
