package com.nahmed.utils;

import com.nahmed.enums.ConfigProperties;

public final class RuntimeConfigResolver {

    private static final String ENV_SYSTEM_PROPERTY = "env";
    private static final String DEFAULT_ENVIRONMENT = "int";

    private RuntimeConfigResolver() {
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.trim().isEmpty()) {
                return value.trim();
            }
        }
        return null;
    }

    private static String normalizeEnvironmentName(String environment) {
        String sanitizedEnvironment = firstNonBlank(environment);
        if (sanitizedEnvironment == null) {
            return DEFAULT_ENVIRONMENT;
        }

        sanitizedEnvironment = sanitizedEnvironment.replace("_", "").trim().toLowerCase();
        return sanitizedEnvironment.isEmpty() ? DEFAULT_ENVIRONMENT : sanitizedEnvironment;
    }

    public static String resolveEnvironmentName() {
        String environmentFromSystemProperty = firstNonBlank(System.getProperty(ENV_SYSTEM_PROPERTY));
        if (environmentFromSystemProperty != null) {
            return normalizeEnvironmentName(environmentFromSystemProperty);
        }

        try {
            return normalizeEnvironmentName(PropertyUtils.getValue(ConfigProperties.ENVIRONMENT));
        } catch (RuntimeException ignored) {
            return DEFAULT_ENVIRONMENT;
        }
    }

    public static String resolveEnvironmentSuffix() {
        return "_" + resolveEnvironmentName();
    }

    public static String resolveThreadCount(String systemPropertyKey) {
        String fromSystemProperty = firstNonBlank(System.getProperty(systemPropertyKey));
        if (fromSystemProperty != null) {
            return fromSystemProperty;
        }

        String fromEnvironmentVariable = firstNonBlank(System.getenv(systemPropertyKey.toUpperCase()));
        if (fromEnvironmentVariable != null) {
            return fromEnvironmentVariable;
        }

        return PropertyUtils.getValue(ConfigProperties.DATAPROVIDER_THREAD_COUNT);
    }

    public static String resolveCucumberTags() {
        String fromSystemProperty = firstNonBlank(
                System.getProperty("cucumber.filter.tags"),
                System.getProperty("tags")
        );
        if (fromSystemProperty != null) {
            return fromSystemProperty;
        }

        return firstNonBlank(
                System.getenv("CUCUMBER_FILTER_TAGS"),
                System.getenv("TAGS")
        );
    }

    public static void applyCucumberTagFilterIfPresent() {
        String resolvedTags = resolveCucumberTags();
        if (resolvedTags != null) {
            System.setProperty("cucumber.filter.tags", resolvedTags);
        }
    }
}