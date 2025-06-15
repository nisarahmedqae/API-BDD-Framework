package com.nahmed.utils;

import com.nahmed.constants.FrameworkConstants;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;

import static org.testng.Assert.fail;

public class ValidationUtils {

    private static final Logger LOG = LogManager.getLogger(ValidationUtils.class);

    public static void validateResponseAgainstSchema(Response response, String schemaFileName) {
        if (response == null) {
            LOG.error("Response object provided for schema validation is null.");
            fail("Cannot validate schema: Response object is null.");
            return;
        }

        // 1. Read the base path from config.properties
        String schemaFolderPath = FrameworkConstants.getSchemaFolderPath();

        //---Check if property exists ---
        if (schemaFolderPath == null || schemaFolderPath.trim().isEmpty()) {
            LOG.error("Schema folder path ('schema_folder_path') is missing or empty in config.properties!");
            fail("Configuration error: Schema folder path ('schema_folder_path') not found in config.properties.");
            return;
        }

        // 2. Construct the full path to the schema file
        String schemaFilePath = schemaFolderPath + schemaFileName;
        LOG.debug("Attempting to validate against schema file: " + schemaFilePath);

        // 3. Create a File object for the schema
        File schemaFile = new File(schemaFilePath);

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