package com.nahmed.utils;

import java.security.SecureRandom;

public class ApiHelper {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

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
