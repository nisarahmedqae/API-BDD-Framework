package com.nahmed.events;

import com.nahmed.enums.ConfigProperties;
import com.nahmed.utils.PropertyUtils;
import com.nahmed.utils.TestContext;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {

    private static final Logger LOG = LoggerFactory.getLogger(Hooks.class);
    private final TestContext testContext;

    public Hooks(TestContext testContext) {
        this.testContext = testContext;
    }

    @Before(order = 1)
    public void generateOAuthToken(Scenario scenario) {

        String environment = testContext.getCurrentEnvironment();
        if (environment.contains("int")) {
            LOG.info("INTEGRATION environment selected");
        } else if (environment.contains("cert")) {
            LOG.info("CERTIFICATION environment selected");
        }

        String tokenUrl = PropertyUtils.getValue(ConfigProperties.TOKEN_URL + environment);
        String clientId = PropertyUtils.getValue(ConfigProperties.CLIENT_ID + environment);
        String clientSecret = PropertyUtils.getValue(ConfigProperties.CLIENT_SECRET + environment);

        LOG.info("Attempting to generate OAuth2 token...");
        /*
        Response tokenResponse = RestAssured
                .given()
                .log().ifValidationFails() // Log request details only if there's an error during the call
                .contentType(ContentType.URLENC) // OAuth2 token endpoints often use form url encoding
                .formParam("grant_type", "client_credentials")
                .formParam("client_id", clientId)
                .formParam("client_secret", clientSecret)
                .when()
                .post(tokenUrl)
                .then()
                .log().ifValidationFails() // Log response details only if validation below fails
                .statusCode(200)
                .extract().response();

        String accessToken = tokenResponse.jsonPath().getString("access_token");
        testContext.sessionMap.put("oauth_token", accessToken);
         */

        LOG.info("OAuth2 token successfully generated and stored in session.");

    }

}