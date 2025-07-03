package com.nahmed.builders;

import com.github.dzieciou.testing.curl.CurlRestAssuredConfigFactory;
import com.github.dzieciou.testing.curl.Options;
import com.nahmed.enums.ConfigProperties;
import com.nahmed.utils.AuthManager;
import com.nahmed.utils.ConfigurationManager;
import com.nahmed.utils.PropertyUtils;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RequestSpecBuilderFactory {

    private static final Logger LOG = LoggerFactory.getLogger(RequestSpecBuilderFactory.class);

    private RequestSpecBuilderFactory() {
    }

    private static RequestSpecification createBaseRequestSpec() {
        RestAssured.baseURI = PropertyUtils.getValue(ConfigProperties.BASE_URL + ConfigurationManager.getCurrentEnvironment());

        // Set up RestAssured to log requests as cURL commands
        Options options = Options.builder()
                .logStacktrace()
                .build();
        RestAssuredConfig config = CurlRestAssuredConfigFactory.createConfig(options);

        // Build the request specification
        return RestAssured.given()
                .config(config)
                .filter(new RestAssuredRequestFilter())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }

    public static RequestSpecification createAuthenticatedRequestSpec() {
        LOG.info("Bearer token successfully generated and stored in session.");
        return createBaseRequestSpec()
                .header("Authorization", AuthManager.getInstance().getBearerToken());
    }

    public static RequestSpecification createRequestSpecWithInvalidToken() {
        LOG.info("Invalid bearer token set in session.");
        return createBaseRequestSpec()
                .header("Authorization", PropertyUtils.getValue(ConfigProperties.INVALID_TOKEN));
    }

    public static RequestSpecification createRequestSpecWithExpiredToken() {
        LOG.info("Expired bearer token set in session.");
        return createBaseRequestSpec()
                .header("Authorization", PropertyUtils.getValue(ConfigProperties.EXPIRED_TOKEN));
    }

}