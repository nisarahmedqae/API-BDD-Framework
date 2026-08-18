package com.nahmed.utils;

import com.nahmed.enums.ConfigProperties;

public final class RuntimeConfigResolver {

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