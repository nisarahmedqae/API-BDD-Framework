package com.nahmed.reports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ExtentLogger {

    private static final Logger LOG = LoggerFactory.getLogger(ExtentLogger.class);

    private ExtentLogger() {
    }

    private static ExtentTest getCurrentTest() { return ExtentManager.getExtentTest(); }

    private static ExtentTest getSafeCurrentTest(String action) {
        ExtentTest currentTest = getCurrentTest();
        if (currentTest == null) {
            LOG.warn("Extent test node is not available for action '{}'.", action);
        }
        return currentTest;
    }

    public static void pass(String message) {
        ExtentTest currentTest = getSafeCurrentTest("pass");
        if (currentTest != null) {
            currentTest.pass(message);
        }
    }

    public static void fail(String message) {
        ExtentTest currentTest = getSafeCurrentTest("fail");
        if (currentTest != null) {
            currentTest.fail(message);
        }
    }

    public static void failDetails(String message) { fail(message); }

    public static void skip(String message) {
        ExtentTest currentTest = getSafeCurrentTest("skip");
        if (currentTest != null) {
            currentTest.skip(message);
        }
    }

    public static void info(String message) {
        ExtentTest currentTest = getSafeCurrentTest("info");
        if (currentTest != null) {
            currentTest.info(message);
        }
    }

    public static void infoInJSON(String jsonContent) {
        ExtentTest currentTest = getSafeCurrentTest("infoInJSON");
        if (currentTest != null) {
            currentTest.info(MarkupHelper.createCodeBlock(jsonContent, CodeLanguage.JSON));
        }
    }

    public static void infoInTable(String[][] tableData) {
        ExtentTest currentTest = getSafeCurrentTest("infoInTable");
        if (currentTest == null) {
            return;
        }

        if (tableData == null || tableData.length == 0) {
            currentTest.info("[No table data]");
            return;
        }

        currentTest.info(MarkupHelper.createTable(tableData));
    }
}