package com.nahmed.events;

import com.nahmed.utils.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
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
    public void setUp() {
        String environment = testContext.getCurrentEnvironment();
        if (environment.contains("int")) {
            LOG.info("INTEGRATION environment selected");
        } else if (environment.contains("cert")) {
            LOG.info("CERTIFICATION environment selected");
        }

        testContext.generateOAuthToken();

    }

    @After(order = 1)
    public void tearDown() {
        RestAssured.reset();
    }

}