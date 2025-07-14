package com.nahmed.utils;

import com.nahmed.constants.FrameworkConstants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;

public class ApiHelper {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // Reads last number from file, increments it, updates the file, and returns the new number
    public static String getNextNumberfromFile() {
        try {
            Path path = Paths.get(FrameworkConstants.getDataStoreFilePath());
            int lastNumber = Files.exists(path)
                    ? Integer.parseInt(Files.readString(path).trim())
                    : 99; // Start from 100
            int nextNumber = lastNumber + 1;
            Files.writeString(path, String.valueOf(nextNumber));
            return String.valueOf(nextNumber);
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("Failed to read/write offer ID from file", e);
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
