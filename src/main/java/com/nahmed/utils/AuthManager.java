package com.nahmed.utils;

import com.nahmed.enums.ConfigProperties;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class AuthManager {

    public String getBearerToken() {

        String tokenUrl = PropertyUtils.getValue(ConfigProperties.TOKEN_URL + ConfigurationManager.getCurrentEnvironment());
        String clientId = PropertyUtils.getValue(ConfigProperties.CLIENT_ID + ConfigurationManager.getCurrentEnvironment());
        String clientSecret = PropertyUtils.getValue(ConfigProperties.CLIENT_SECRET + ConfigurationManager.getCurrentEnvironment());

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

        return tokenResponse.jsonPath().getString("access_token");
         */
        return "Bearer mockToken";
    }
}