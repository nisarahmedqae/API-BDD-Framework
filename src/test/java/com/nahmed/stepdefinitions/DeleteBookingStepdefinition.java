package com.nahmed.stepdefinitions;

import com.nahmed.builders.RequestSpecBuilderFactory;
import com.nahmed.utils.TestContext;
import com.nahmed.utils.TestContext;

import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteBookingStepdefinition {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteBookingStepdefinition.class);
    private final TestContext testContext;
    private final RequestSpecBuilderFactory requestSpecFactory;

    public DeleteBookingStepdefinition(TestContext testContext, RequestSpecBuilderFactory requestSpecFactory) {
        this.testContext = testContext;
        this.requestSpecFactory = requestSpecFactory;
    }

    @When("user makes a request to delete booking with basic auth {string} & {string}")
    public void userMakesARequestToDeleteBookingWithBasicAuth(String username, String password) {
        Response response = requestSpecFactory.createAuthenticatedRequestSpec()
                .auth().preemptive().basic(username, password)
                .pathParam("bookingID", testContext.getData("bookingID", Integer.class))
                .when().delete(testContext.getData("endpoint", String.class) + "/{bookingID}");
        testContext.setResponse(response);
    }
}
