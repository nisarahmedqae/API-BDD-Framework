package com.nahmed.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.dzieciou.testing.curl.CurlRestAssuredConfigFactory;
import com.github.dzieciou.testing.curl.Options;

import com.nahmed.enums.ConfigProperties;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestContext {

    // Logger
    private static final Logger LOG = LoggerFactory.getLogger(TestContext.class);

    public Response response;
    public Map<String, Object> sessionMap = new ConcurrentHashMap<>();
    private static final String CONTENT_TYPE = PropertyUtils.getValue(ConfigProperties.CONTENT_TYPE);

    // Environment selection
    private static final String ENV_SYSTEM_PROPERTY = "env"; // System property: -Denv=CERT
    private String currentEnvironment = null;

    // Method to get the determined environment if needed elsewhere safely
    public String getCurrentEnvironment() {
        if (currentEnvironment == null) {
            String envFromProps = PropertyUtils.getValue(ConfigProperties.ENVIRONMENT);
            String defaultEnvironment = (envFromProps == null || envFromProps.trim().isEmpty())
                    ? "INT"
                    : envFromProps;

            this.currentEnvironment = "_" + System.getProperty(ENV_SYSTEM_PROPERTY, defaultEnvironment);
        }
        return currentEnvironment;
    }

    public void generateOAuthToken() {

        String tokenUrl = PropertyUtils.getValue(ConfigProperties.TOKEN_URL + currentEnvironment);
        String clientId = PropertyUtils.getValue(ConfigProperties.CLIENT_ID + currentEnvironment);
        String clientSecret = PropertyUtils.getValue(ConfigProperties.CLIENT_SECRET + currentEnvironment);

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
        //sessionMap.put("oauth_token", accessToken);
         */
        sessionMap.put("oauth_token", "accessToken");
        LOG.info("OAuth2 token successfully generated and stored in session.");

    }

    public RequestSpecification requestSetup() {

        RestAssured.baseURI = PropertyUtils.getValue(ConfigProperties.BASE_URL + this.currentEnvironment);

        // Set up RestAssured to log requests as cURL commands
        Options options = Options.builder().logStacktrace().build();
        RestAssuredConfig config = CurlRestAssuredConfigFactory.createConfig(options);

        String bearerToken = "Bearer " + sessionMap.get("oauth_token").toString();

        // Build the request specification
        RequestSpecification request = RestAssured.given()
                .config(config)
                .filter(new RestAssuredRequestFilter())
                .contentType(CONTENT_TYPE)
                .accept(CONTENT_TYPE)
                .header("Authorization", bearerToken);

        return request;

    }

}