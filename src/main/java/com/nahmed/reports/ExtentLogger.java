package com.nahmed.reports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.nahmed.enums.ConfigProperties;
import com.nahmed.utils.PropertyUtils;
import com.nahmed.utils.ScreenshotUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ExtentLogger {

    private static final Logger LOG = LoggerFactory.getLogger(ExtentLogger.class);

    private enum ScreenshotMode {
        INLINE,
        FILE
    }

    private ExtentLogger() {
    }

    private static boolean isEnabled(ConfigProperties property) {
        String value = PropertyUtils.getValue(property);
        return value != null && value.equalsIgnoreCase("yes");
    }

    private static ExtentTest getCurrentTest() { return ExtentManager.getExtentTest(); }

    private static ScreenshotMode resolveScreenshotMode() {
        String configuredMode;
        try {
            configuredMode = PropertyUtils.getValue(ConfigProperties.SCREENSHOT_MODE);
        } catch (RuntimeException ignored) {
            return ScreenshotMode.INLINE;
        }

        if (configuredMode == null) {
            return ScreenshotMode.INLINE;
        }

        String normalizedMode = configuredMode.trim().toLowerCase();
        if ("file".equals(normalizedMode)) {
            return ScreenshotMode.FILE;
        }
        return ScreenshotMode.INLINE;
    }

    public static void pass(String message) {
        ExtentTest currentTest = getCurrentTest();
        if (currentTest == null) {
            LOG.warn("Extent test node is not available for thread '{}'. Message: {}",
                    Thread.currentThread().getName(), message);
            return;
        }

        if (isEnabled(ConfigProperties.PASSED_STEPS_SCREENSHOTS)) {
            addWithScreenshot(currentTest, LogLevel.PASS, message);
        } else {
            currentTest.pass(message);
        }
    }

    public static void fail(String message) {
        ExtentTest currentTest = getCurrentTest();
        if (currentTest == null) {
            LOG.warn("Extent test node is not available for thread '{}'. Message: {}",
                    Thread.currentThread().getName(), message);
            return;
        }

        if (isEnabled(ConfigProperties.FAILED_STEPS_SCREENSHOTS)) {
            addWithScreenshot(currentTest, LogLevel.FAIL, message);
        } else {
            currentTest.fail(message);
        }
    }

    public static void failDetails(String message) {
        ExtentTest currentTest = getCurrentTest();
        if (currentTest == null) {
            LOG.warn("Extent test node is not available for thread '{}'. Message: {}",
                    Thread.currentThread().getName(), message);
            return;
        }

        currentTest.fail(message);
    }

    public static void skip(String message) {
        ExtentTest currentTest = getCurrentTest();
        if (currentTest == null) {
            LOG.warn("Extent test node is not available for thread '{}'. Message: {}",
                    Thread.currentThread().getName(), message);
            return;
        }

        if (isEnabled(ConfigProperties.SKIPPED_STEPS_SCREENSHOTS)) {
            addWithScreenshot(currentTest, LogLevel.SKIP, message);
        } else {
            currentTest.skip(message);
        }
    }

    private static void addWithScreenshot(ExtentTest currentTest, LogLevel level, String message) {
        try {
            if (resolveScreenshotMode() == ScreenshotMode.FILE) {
                String screenshotPath = ScreenshotUtils.captureScreenshotToFile();

                if (level == LogLevel.PASS) {
                    currentTest.pass(message,
                            MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                } else if (level == LogLevel.FAIL) {
                    currentTest.fail(message,
                            MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                } else {
                    currentTest.skip(message,
                            MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                }
            } else {
                String base64Image = ScreenshotUtils.getBase64Image();

                if (level == LogLevel.PASS) {
                    currentTest.pass(message,
                            MediaEntityBuilder.createScreenCaptureFromBase64String(base64Image).build());
                } else if (level == LogLevel.FAIL) {
                    currentTest.fail(message,
                            MediaEntityBuilder.createScreenCaptureFromBase64String(base64Image).build());
                } else {
                    currentTest.skip(message,
                            MediaEntityBuilder.createScreenCaptureFromBase64String(base64Image).build());
                }
            }
        } catch (RuntimeException screenshotError) {
            LOG.warn("Unable to attach screenshot. Logging text only. Cause: {}", screenshotError.getMessage());
            if (level == LogLevel.PASS) {
                currentTest.pass(message);
            } else if (level == LogLevel.FAIL) {
                currentTest.fail(message);
            } else {
                currentTest.skip(message);
            }
        }
    }

    private enum LogLevel {
        PASS, FAIL, SKIP
    }
}