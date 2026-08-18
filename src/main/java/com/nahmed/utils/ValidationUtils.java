package com.nahmed.utils;

import com.nahmed.constants.FrameworkConstants;
import com.nahmed.reports.ExtentLogger;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;

import static org.testng.Assert.fail;

public class ValidationUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ValidationUtils.class);

    public static void validateResponseAgainstSchema(Response response, String schemaFileName) {
        if (response == null) {
            LOG.error("Response object provided for schema validation is null.");
            fail("Cannot validate schema: Response object is null.");
        }

        if (schemaFileName == null || schemaFileName.trim().isEmpty()) {
            LOG.error("Schema file name provided for validation is null or empty.");
            fail("Cannot validate schema: Schema file name is null or empty.");
        }

        // 1. Resolve the base path for schema files
        String schemaFolderPath = FrameworkConstants.getSchemaFolderPath();

        if (schemaFolderPath.trim().isEmpty()) {
            LOG.error("Schema folder path is empty.");
            fail("Configuration error: Schema folder path is empty.");
        }

        // 2. Construct the full path to the schema file
        String schemaFilePath = Path.of(schemaFolderPath, schemaFileName).toString();
        LOG.debug("Attempting to validate against schema file: " + schemaFilePath);

        // 3. Create a File object for the schema
        File schemaFile = new File(schemaFilePath);

        // --- Check if file actually exists ---
        if (!schemaFile.exists() || !schemaFile.isFile()) {
            LOG.error("Schema file not found or is not a file at the specified path: " + schemaFile.getAbsolutePath());
            fail("Schema file not found or is not a file: " + schemaFile.getAbsolutePath());
        }

        // 4. Perform the validation using matchesJsonSchema(File)
        try {
            response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(schemaFile));
            LOG.info("Successfully validated response against schema: " + schemaFileName);
            ExtentLogger.pass("Successfully validated response against schema: " + schemaFileName);
        } catch (AssertionError e) {
            LOG.error("Schema validation failed for: " + schemaFileName + ". Error: " + e.getMessage());
            ExtentLogger.fail("Schema validation failed for: " + schemaFileName + ". Error: " + e.getMessage());
            throw e; // Re-throw the assertion error so the test fails correctly
        } catch (Exception e) {
            LOG.error("An unexpected error occurred during schema validation for: " + schemaFileName, e);
            ExtentLogger.fail("An unexpected error occurred during schema validation for: " + schemaFileName);
            fail("Unexpected error during schema validation: " + e.getMessage());
        }
    }
}