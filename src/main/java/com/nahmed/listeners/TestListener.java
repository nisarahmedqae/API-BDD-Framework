package com.nahmed.listeners;

import com.nahmed.reports.ExtentLogger;
import com.nahmed.reports.ExtentReport;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestListener implements ConcurrentEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(TestListener.class);

    private String getStepDescription(PickleStepTestStep pickleStep) {
        String stepText = pickleStep.getStep().getText();
        String keyword = pickleStep.getStep().getKeyword();
        return keyword.trim() + " " + stepText;
    }

    private String getHookDescription(HookTestStep hookStep) {
        return "HOOK " + hookStep.getHookType() + " : " + hookStep.getCodeLocation();
    }

    private String getErrorMessage(Throwable error) {
        if (error == null) {
            return "No error details available";
        }
        return error.getMessage() == null ? error.toString() : error.getMessage();
    }

    private void logStepOutcome(Status status, String stepDescription, Throwable error) {
        String statusPrefix = "  STEP ";

        switch (status) {
            case PASSED:
                ExtentLogger.pass(stepDescription + " is PASSED");
                LOG.info(statusPrefix + status + ": " + stepDescription);
                break;
            case FAILED:
                ExtentLogger.fail(stepDescription + " is FAILED");
                if (error != null) {
                    ExtentLogger.failDetails("Failure Cause: " + getErrorMessage(error));
                    LOG.error("  Underlying Step Failure Cause: {}", getErrorMessage(error));
                }
                LOG.error(statusPrefix + status + ": " + stepDescription);
                break;
            case SKIPPED:
                ExtentLogger.skip(stepDescription + " is SKIPPED");
                if (error != null) {
                    LOG.info("  Reason for Skip: {}", error.getMessage());
                }
                LOG.info(statusPrefix + status + ": " + stepDescription);
                break;
            case PENDING:
                ExtentLogger.skip(stepDescription + " is PENDING");
                LOG.info(statusPrefix + status + ": " + stepDescription);
                break;
            case UNDEFINED:
                ExtentLogger.skip(stepDescription + " is UNDEFINED (step definition missing)");
                LOG.info(statusPrefix + status + ": " + stepDescription);
                break;
            case AMBIGUOUS:
                ExtentLogger.fail(stepDescription + " is AMBIGUOUS (multiple step definitions found)");
                if (error != null) {
                    ExtentLogger.failDetails("Ambiguity Details: " + getErrorMessage(error));
                    LOG.error("  Ambiguity Cause: {}", getErrorMessage(error));
                }
                LOG.error(statusPrefix + status + ": " + stepDescription);
                break;
            default:
                ExtentLogger.skip(stepDescription + " has unhandled status: " + status);
                LOG.warn(statusPrefix + status + ": " + stepDescription);
                break;
        }
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        // Suite-level events
        publisher.registerHandlerFor(TestRunStarted.class, this::handleTestRunStarted);
        publisher.registerHandlerFor(TestRunFinished.class, this::handleTestRunFinished);

        // Scenario-level events
        publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
        publisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinished);

        // Step-level events
        publisher.registerHandlerFor(TestStepStarted.class, this::handleTestStepStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
    }

    // --- Handler for Test Run Started (Suite Start) ---
    private void handleTestRunStarted(TestRunStarted event) {
        LOG.info("================================================================================");
        LOG.info(" CUCUMBER TEST EXECUTION STARTED ");
        LOG.info(" Timestamp: {}", event.getInstant());
        LOG.info("================================================================================");

        ExtentReport.initReports();
    }

    // --- Handler for Test Run Finished (Suite Finish) ---
    private void handleTestRunFinished(TestRunFinished event) {
        Result result = event.getResult(); // Overall result of the test run
        Status status = result.getStatus(); // This can be PASSED if all scenarios passed, FAILED otherwise

        LOG.info("================================================================================");
        LOG.info(" CUCUMBER TEST EXECUTION FINISHED ");
        LOG.info(" Overall Status: {}", status.name());
        LOG.info(" Timestamp: {}", event.getInstant());
        LOG.info("================================================================================");

        ExtentReport.flushReports();
    }

    // --- Handler for Scenario Started ---
    private void handleTestCaseStarted(TestCaseStarted event) {
        TestCase testCase = event.getTestCase();
        String testCaseName = testCase.getName();
        String featureName = testCase.getUri().toString().substring(testCase.getUri().toString().lastIndexOf('/') + 1);
        ExtentReport.createTest(featureName + " : " + testCaseName);

        LOG.info("********************************************************************************");
        LOG.info("Feature: {}", featureName);
        LOG.info("Starting Scenario: {} (Line: {})", testCaseName, testCase.getLine());
        LOG.info("Tags: {}", String.join(", ", testCase.getTags()));
        LOG.info("********************************************************************************");
    }

    // --- Handler for Scenario Finished ---
    private void handleTestCaseFinished(TestCaseFinished event) {
        TestCase testCase = event.getTestCase();
        String testCaseName = testCase.getName();
        Result result = event.getResult();
        Status status = result.getStatus();
        String outcomePrefix = "Finished Scenario: ";

        LOG.info("********************************************************************************");
        LOG.info("{}{} -> {}", outcomePrefix, testCaseName, status.name());
        LOG.info("  Duration: {} seconds", String.format("%.2f", result.getDuration().toMillis() / 1000.0));
        LOG.info("********************************************************************************");
        LOG.info("");

        ExtentReport.clearScenarioContext();
    }

    // --- Handler for Step Started ---
    private void handleTestStepStarted(TestStepStarted event) {
        TestStep testStep = event.getTestStep();
        if (testStep instanceof PickleStepTestStep) {
            PickleStepTestStep pickleStep = (PickleStepTestStep) testStep;
            String stepDescription = getStepDescription(pickleStep);
            LOG.info("  {}", stepDescription);

            ExtentReport.addTestStep(stepDescription);
        } else if (testStep instanceof HookTestStep) {
            HookTestStep hookStep = (HookTestStep) testStep;
            String hookDescription = getHookDescription(hookStep);
            LOG.info("  {}", hookDescription);

            ExtentReport.addTestStep(hookDescription);
        }
    }

    // --- Handler for Step Finished ---
    private void handleTestStepFinished(TestStepFinished event) {
        TestStep testStep = event.getTestStep();
        Result result = event.getResult();
        Status status = result.getStatus();
        Throwable error = result.getError();

        if (testStep instanceof PickleStepTestStep) {
            PickleStepTestStep pickleStep = (PickleStepTestStep) testStep;
            String stepDescription = getStepDescription(pickleStep);
            logStepOutcome(status, stepDescription, error);
        } else if (testStep instanceof HookTestStep) {
            HookTestStep hookStep = (HookTestStep) testStep;
            String hookDescription = getHookDescription(hookStep);
            logStepOutcome(status, hookDescription, error);
        }
    }
}