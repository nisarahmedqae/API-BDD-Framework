package com.nahmed.cucumber.utils;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;

import static org.testng.Assert.fail; // Import Assert.fail for explicit failures

public class ValidationUtils {

    private static final Logger LOG = LogManager.getLogger(ValidationUtils.class);

    /**
     * Validates the body of a RestAssured Response against a JSON schema file.
     * Reads the base schema path from config.properties ("schema.path").
     * Assumes the schema path in config.properties is relative to the project root.
     *
     * @param response       The RestAssured Response object to validate.
     * @param schemaFileName The name of the schema file (e.g., "createBookingSchema.json").
     */

    public static void validateResponseAgainstSchema(Response response, String schemaFileName) {
        // --- Pre-checks ---
        if (response == null) {
            LOG.error("Response object provided for schema validation is null.");
            fail("Cannot validate schema: Response object is null.");
            return;
        }
        if (schemaFileName == null || schemaFileName.trim().isEmpty()) {
            LOG.error("Schema file name provided for validation is null or empty.");
            fail("Cannot validate schema: Schema file name is null or empty.");
            return;
        }

        // 1. Read the base path from config.properties
        String schemaBasePath = PropertiesFile.getProperty("schema.path");

        //---Check if property exists ---
        if (schemaBasePath == null || schemaBasePath.trim().isEmpty()) {
            LOG.error("Schema base path ('schema.path') is missing or empty in config.properties!");
            fail("Configuration error: Schema base path ('schema.path') not found in config.properties.");
            return;
        }

        // 2. Construct the full path to the schema file
        String fullSchemaPath = schemaBasePath + schemaFileName;
        LOG.debug("Attempting to validate against schema file: " + fullSchemaPath);

        // 3. Create a File object for the schema
        File schemaFile = new File(fullSchemaPath);

        // --- Check if file actually exists ---
        if (!schemaFile.exists() || !schemaFile.isFile()) {
            LOG.error("Schema file not found or is not a file at the specified path: " + schemaFile.getAbsolutePath());
            fail("Schema file not found or is not a file: " + schemaFile.getAbsolutePath());
            return;
        }

        // 4. Perform the validation using matchesJsonSchema(File)
        try {
            response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(schemaFile));
            LOG.info("Successfully validated response against schema: " + schemaFileName);
        } catch (AssertionError e) {
            LOG.error("Schema validation failed for: " + schemaFileName + ". Error: " + e.getMessage());
            throw e; // Re-throw the assertion error so the test fails correctly
        } catch (Exception e) {
            LOG.error("An unexpected error occurred during schema validation for: " + schemaFileName, e);
            fail("Unexpected error during schema validation: " + e.getMessage());
        }
    }
}