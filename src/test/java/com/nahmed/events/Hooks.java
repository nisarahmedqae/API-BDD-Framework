package com.nahmed.events;

import com.nahmed.utils.RuntimeConfigResolver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {

    private static final Logger LOG = LoggerFactory.getLogger(Hooks.class);

    @Before(order = 1)
    public void setUp(Scenario scenario) {
        String currentEnvironment = RuntimeConfigResolver.resolveEnvironmentSuffix();
        String normalizedEnvironment = currentEnvironment.replace("_", "").toUpperCase();
        switch (normalizedEnvironment) {
            case "INT":
                LOG.info("Environment selected: INTEGRATION");
                break;
            case "CERT":
                LOG.info("Environment selected: CERTIFICATION");
                break;
            default:
                LOG.warn("Default environment selected: {}", currentEnvironment);
                break;
        }

        LOG.info("Starting scenario: {} | tags: {}", scenario.getName(), scenario.getSourceTagNames());
    }

    @After(order = 1)
    public void tearDown(Scenario scenario) {
        LOG.info("Finished scenario: {} | status: {}", scenario.getName(), scenario.getStatus());
    }

}