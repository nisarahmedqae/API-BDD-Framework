package com.nahmed.events;

import com.nahmed.enums.ConfigProperties;
import com.nahmed.utils.ConfigurationManager;
import com.nahmed.utils.PropertyUtils;
import com.nahmed.utils.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {

    private static final Logger LOG = LoggerFactory.getLogger(Hooks.class);
    private final TestContext testContext;

    public Hooks(TestContext testContext) {
        this.testContext = testContext;
    }

    @Before(order = 1)
    public void setUp(Scenario scenario) {

        String currentEnvironment = ConfigurationManager.getCurrentEnvironment();
        switch (currentEnvironment.toUpperCase()) {
            case "_INT":
                LOG.info("Environment selected: INTEGRATION");
                break;
            case "_CERT":
                LOG.info("Environment selected: CERTIFICATION");
                break;
            default:
                LOG.warn("Default environment selected: {}", currentEnvironment);
                break;
        }

    }

    @After(order = 1)
    public void tearDown() {
        RestAssured.reset();
    }

}