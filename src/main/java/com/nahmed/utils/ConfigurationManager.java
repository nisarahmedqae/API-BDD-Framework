package com.nahmed.utils;

import com.nahmed.enums.ConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConfigurationManager {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationManager.class);

    private ConfigurationManager() {
    }

    private static final String ENV_SYSTEM_PROPERTY = "env"; // System property: -Denv=CERT
    private static final String currentEnvironment;

    private static String resolveDefaultEnvironment() {
        try {
            String envFromProps = PropertyUtils.getValue(ConfigProperties.ENVIRONMENT);
            return (envFromProps == null || envFromProps.trim().isEmpty()) ? "INT" : envFromProps.trim();
        } catch (RuntimeException e) {
            LOG.warn("Could not resolve default environment from config. Falling back to INT. Cause: {}", e.getMessage());
            return "INT";
        }
    }

    // Static initializer block: runs once and is thread-safe
    static {
        String defaultEnvironment = resolveDefaultEnvironment();
        currentEnvironment = "_" + System.getProperty(ENV_SYSTEM_PROPERTY, defaultEnvironment);
    }

    public static String getCurrentEnvironment() { return currentEnvironment; }

}