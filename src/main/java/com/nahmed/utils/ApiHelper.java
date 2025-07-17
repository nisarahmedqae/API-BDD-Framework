package com.nahmed.utils;

import com.nahmed.constants.FrameworkConstants;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Properties;

public class ApiHelper {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // Method to read a data_store property value
    public static String getDataStore(String key) {
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
    public static void setDataStore(String key, String value) {
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
