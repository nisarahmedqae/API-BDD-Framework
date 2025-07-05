package com.nahmed.factories;

import com.github.dzieciou.testing.curl.CurlRestAssuredConfigFactory;
import com.github.dzieciou.testing.curl.Options;
import com.nahmed.enums.ConfigProperties;
import com.nahmed.utils.AuthManager;
import com.nahmed.utils.ConfigurationManager;
import com.nahmed.utils.PropertyUtils;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestSpecBuilderFactory {

    private static final Logger LOG = LoggerFactory.getLogger(RequestSpecBuilderFactory.class);
    private final AuthManager authManager;
    private final String baseUrl;

    public RequestSpecBuilderFactory(AuthManager authManager) {
        this.authManager = authManager;
        this.baseUrl = PropertyUtils.getValue(ConfigProperties.BASE_URL + ConfigurationManager.getCurrentEnvironment());
    }

    private RequestSpecification createBaseRequestSpec() {
        // Set up RestAssured to log requests as cURL commands
        Options options = Options.builder()
                .logStacktrace()
                .build();
        RestAssuredConfig config = CurlRestAssuredConfigFactory.createConfig(options);

        // Build the request specification
        return RestAssured.given()
                .baseUri(this.baseUrl)
                .config(config)
                .filter(new RestAssuredRequestFilter())
                .header("Content-Type", "application/json; charset=UTF-8")
                .header("Accept", "application/json");
    }

    public RequestSpecification createRequestSpec() {
        LOG.info("Creating a new unauthenticated request specification.");
        return createBaseRequestSpec();
    }

    public RequestSpecification createAuthenticatedRequestSpec() {
        LOG.info("Bearer token successfully generated and stored in session.");
        return createBaseRequestSpec()
                .header("Authorization", authManager.getBearerToken());
    }

    public RequestSpecification createRequestSpecWithInvalidToken() {
        LOG.info("Invalid bearer token set in session.");
        return createBaseRequestSpec()
                .header("Authorization", PropertyUtils.getValue(ConfigProperties.INVALID_TOKEN));
    }

    public RequestSpecification createRequestSpecWithExpiredToken() {
        LOG.info("Expired bearer token set in session.");
        return createBaseRequestSpec()
                .header("Authorization", PropertyUtils.getValue(ConfigProperties.EXPIRED_TOKEN));
    }

}