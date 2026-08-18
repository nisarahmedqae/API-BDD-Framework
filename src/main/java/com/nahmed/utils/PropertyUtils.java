package com.nahmed.utils;

import com.nahmed.constants.FrameworkConstants;
import com.nahmed.enums.ConfigProperties;
import com.nahmed.exceptions.PropertyFileUsageException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public final class PropertyUtils {

    private PropertyUtils() {
    }

    private static final Map<String, String> CONFIGMAP;

    static {
        Map<String, String> loadedConfigMap = new java.util.HashMap<>();
        try (FileInputStream fis = new FileInputStream(FrameworkConstants.getConfigFilePath())) {
            Properties prop = new Properties();
            prop.load(fis);

            for (Map.Entry<Object, Object> entry : prop.entrySet()) {
                loadedConfigMap.put(entry.getKey().toString().toLowerCase(), entry.getValue().toString());
            }

        } catch (IOException e) {
            throw new ExceptionInInitializerError("Failed to load config.properties: " + e.getMessage());
        }

        CONFIGMAP = Collections.unmodifiableMap(loadedConfigMap);
    }

    public static String getValue(ConfigProperties key) {
        return getValueInternal(key, key == null ? null : key.toString().toLowerCase());
    }

    public static String getValue(String key) {
        return getValueInternal(key, key == null ? null : key.toLowerCase());
    }

    private static String getValueInternal(Object originalKey, String normalizedKey) {
        if (Objects.isNull(originalKey) || Objects.isNull(CONFIGMAP.get(normalizedKey))) {
            throw new PropertyFileUsageException(
                    "Property name " + originalKey + " is not found. Please check config.properties"
            );
        }
        return CONFIGMAP.get(normalizedKey);
    }
}