package com.nahmed.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TestContext {
/*
    private static final Logger LOG = LoggerFactory.getLogger(TestContext.class);
    private static final TestContext instance = new TestContext();
    private final Map<String, Object> store = new ConcurrentHashMap<>();

    private TestContext() {
    }

    public static TestContext getInstance() {
        return instance;
    }

    public void set(String key, Object value) {
        LOG.debug("[Thread: {}] Storing in context: Key='{}'", Thread.currentThread().getId(), key);
        store.put(key, value);
    }

    public <T> T get(String key, Class<T> type) {
        Object value = store.get(key);
        LOG.debug("[Thread: {}] Retrieving from context: Key='{}'", Thread.currentThread().getId(), key);
        return (value == null) ? null : type.cast(value);
    }

    public void remove(String key) {
        store.remove(key);
    }

 */

}