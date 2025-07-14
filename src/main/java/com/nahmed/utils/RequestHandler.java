package com.nahmed.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RequestHandler.class);
    private static final ObjectMapper mapper = new ObjectMapper(); // Reuse ObjectMapper

    public static String prettyPrint(String rawJson) {
        try {
            Object json = mapper.readValue(rawJson, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        } catch (Exception e) {
            LOG.error("Failed to pretty print request body", e);
            return rawJson;
        }
    }
}
