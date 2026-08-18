package com.nahmed.utils;

import com.nahmed.constants.FrameworkConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Properties;

public class ApiHelper {

    private static final Logger LOG = LoggerFactory.getLogger(ApiHelper.class);

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Object DATA_STORE_LOCK = new Object();

    private static Properties loadDataStoreProperties() throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(FrameworkConstants.getDataStoreFilePath())) {
            properties.load(fis);
        }
        return properties;
    }

    private static void storeDataStoreProperties(Properties properties) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(FrameworkConstants.getDataStoreFilePath())) {
            properties.store(fos, "Updated during testing");
        }
    }

    // Method to read a data_store property value
    public static String getDataStore(String key) {
        synchronized (DATA_STORE_LOCK) {
            try {
                Properties properties = loadDataStoreProperties();
                return properties.getProperty(key);
            } catch (IOException e) {
                LOG.error("Failed to read '{}' from data store '{}'.", key, FrameworkConstants.getDataStoreFilePath(), e);
                return null;
            }
        }
    }

    // Method to update a data_store property value
    public static void setDataStore(String key, String value) {
        synchronized (DATA_STORE_LOCK) {
            Properties properties;
            try {
                properties = loadDataStoreProperties();
            } catch (IOException e) {
                LOG.warn("Could not load existing data store '{}'. A new in-memory properties set will be used for this write. Cause: {}",
                        FrameworkConstants.getDataStoreFilePath(), e.getMessage());
                properties = new Properties();
            }

            properties.setProperty(key, value);

            try {
                storeDataStoreProperties(properties);
            } catch (IOException e) {
                LOG.error("Failed to write '{}' to data store '{}'.", key, FrameworkConstants.getDataStoreFilePath(), e);
            }
        }
    }

    // Helper to generate random alphanumeric string
    public static String generateRandomAlphanumeric(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUMERIC.charAt(SECURE_RANDOM.nextInt(ALPHANUMERIC.length())));
        }
        return sb.toString();
    }

    // Generates 13-char alphanumeric orderId
    public static String generateOrderId() {
        return generateRandomAlphanumeric(13);
    }

    // Generates 6-char alphanumeric orderId
    public static String generateShoppingTransactionId() {
        return generateRandomAlphanumeric(6);
    }

    // Generates 6-char alphabetic PNR locator
    public static String generatePnrLocator() {
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(ALPHABET.charAt(SECURE_RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}