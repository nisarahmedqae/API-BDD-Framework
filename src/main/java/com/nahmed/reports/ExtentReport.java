package com.nahmed.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.nahmed.constants.FrameworkConstants;
import com.nahmed.utils.RuntimeConfigResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public final class ExtentReport {

    private static final Logger LOG = LoggerFactory.getLogger(ExtentReport.class);

    private ExtentReport() {
    }

    private static volatile ExtentReports extent;

    public static synchronized void initReports() {
        if (Objects.isNull(extent)) {
            String environmentName = RuntimeConfigResolver.resolveEnvironmentName().toUpperCase(Locale.ROOT);
            extent = new ExtentReports();
            ExtentSparkReporter spark = new ExtentSparkReporter(FrameworkConstants.getExtentReportFilePath());
            extent.attachReporter(spark);
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle("Test Results");
            spark.config().setReportName("restassured-bdd-framework - " + environmentName);
            extent.setSystemInfo("Environment", environmentName);
        }
    }

    public static void flushReports() {
        try {
            if (Objects.nonNull(extent)) {
                extent.flush();
            }

            if (!Desktop.isDesktopSupported()) {
                return;
            }

            Desktop desktop = Desktop.getDesktop();
            if (!desktop.isSupported(Desktop.Action.BROWSE)) {
                return;
            }

            String osName = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
            if (osName.contains("linux") && System.getenv("CI") != null) {
                return;
            }

            desktop.browse(new File(FrameworkConstants.getExtentReportFilePath()).toURI());
        } catch (IOException | RuntimeException e) {
            LOG.error("Unable to open extent report in browser", e);
        } finally {
            clearScenarioContext();
        }
    }

    public static void createTest(String testCaseName) {
        if (extent == null) {
            initReports();
        }

        ExtentTest scenarioTest;
        synchronized (ExtentReport.class) {
            scenarioTest = extent.createTest(testCaseName);
        }

        ExtentStepManager.setExtentTestStep(scenarioTest);
        ExtentManager.setExtentTest(scenarioTest); // Set scenario as current test in ExtentManager
    }

    public static void addTestStep(String stepDescription) {
        ExtentTest currentScenario = ExtentStepManager.getExtentTestStep();
        if (currentScenario != null) {
            ExtentTest stepTest = currentScenario.createNode(stepDescription);
            ExtentManager.setExtentTest(stepTest); // Now, ExtentManager's context is this step
        } else {
            LOG.error("Cannot add step '{}'. Current scenario test not found in ExtentReport.", stepDescription);
        }
    }

    public static void clearScenarioContext() {
        ExtentManager.unload();
        ExtentStepManager.unload();
    }
}