package com.nahmed.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.dzieciou.testing.curl.CurlRestAssuredConfigFactory;
import com.github.dzieciou.testing.curl.Options;

import com.nahmed.enums.ConfigProperties;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
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

    // Constants for environment selection
    private static final String ENV_SYSTEM_PROPERTY = "env"; // System property: -Denv=CERT

    // Store the determined environment for potential use elsewhere (like Hooks)
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

    public RequestSpecification requestSetup() {
        RestAssured.reset();

        // Set the BaseURL
        RestAssured.baseURI = PropertyUtils.getValue(ConfigProperties.BASE_URL + this.currentEnvironment);

        // --- RestAssured Configuration ---
        Options options = Options.builder().logStacktrace().build();
        RestAssuredConfig config = CurlRestAssuredConfigFactory.createConfig(options);

        // Build the request specification
        RequestSpecification request = RestAssured.given()
                .config(config)
                .filter(new RestAssuredRequestFilter())
                .contentType(CONTENT_TYPE)
                .accept(CONTENT_TYPE);

        if (sessionMap.containsKey("oauth_token")) {
            String bearerToken = "Bearer " + sessionMap.get("oauth_token").toString();
            LOG.debug("Adding Authentication Bearer Header from session.");
            request = request.header("Authorization", bearerToken);
        }

        return request;
    }

}