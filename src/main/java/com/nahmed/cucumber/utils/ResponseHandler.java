package com.nahmed.cucumber.utils;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
// --- ADD SLF4j Imports ---
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.restassured.response.Response;

public class ResponseHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseHandler.class);
    private static final ObjectMapper mapper = new ObjectMapper(); // Reuse ObjectMapper

    public static <T> T deserializedResponse(Response response, Class<T> clazz) { // Use Class<T>
        T responseDeserialized = null;
        try {
            String responseBody = response.asString(); // Get body once
            responseDeserialized = mapper.readValue(responseBody, clazz);

            if (LOG.isDebugEnabled()) {
                String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDeserialized);
                LOG.debug("Handling Response (Deserialized): \n{}", jsonStr);
                LOG.debug("Successfully deserialized response to type: {}", clazz.getName());
            }

        } catch (IOException e) {
            LOG.error("Error deserializing response body to {}. Response Body:\n{}", clazz.getName(), response.asString(), e);
        }
        return responseDeserialized;
    }
}