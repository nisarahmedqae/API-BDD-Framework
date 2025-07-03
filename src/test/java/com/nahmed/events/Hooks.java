package com.nahmed.events;

import com.nahmed.enums.ConfigProperties;
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
        String environment = testContext.getCurrentEnvironment();
        if (environment.contains("int")) {
            LOG.info("INTEGRATION environment selected");
        } else if (environment.contains("cert")) {
            LOG.info("CERTIFICATION environment selected");
        }


        if (scenario.getSourceTagNames().contains("@invalid_token")) {
            String invalidToken = PropertyUtils.getValue(ConfigProperties.INVALID_TOKEN);
            testContext.sessionMap.put("access_token", "Bearer " + invalidToken);
            LOG.info("Invalid bearer token set in session.");
        } else if (scenario.getSourceTagNames().contains("@expired_token")) {
            String expiredToken = PropertyUtils.getValue(ConfigProperties.EXPIRED_TOKEN);
            testContext.sessionMap.put("access_token", "Bearer " + expiredToken);
            LOG.info("Expired bearer token set in session.");
        } else {
            String accessToken = testContext.generateOAuthToken();
            testContext.sessionMap.put("access_token", "Bearer " + accessToken);
            LOG.info("Bearer token successfully generated and stored in session.");
        }

    }

    @After(order = 1)
    public void tearDown() {
        RestAssured.reset();
    }

}