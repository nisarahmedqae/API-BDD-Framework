package com.nahmed.stepdefinitions;

import com.nahmed.builders.RequestSpecBuilderFactory;
import com.nahmed.utils.ScenarioState;
import com.nahmed.utils.TestContext;

import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteBookingStepdefinition {

    private final ScenarioState scenarioState;
    private static final Logger LOG = LoggerFactory.getLogger(DeleteBookingStepdefinition.class);

    public DeleteBookingStepdefinition(ScenarioState scenarioState) {
        this.scenarioState = scenarioState;
    }

    @When("user makes a request to delete booking with basic auth {string} & {string}")
    public void userMakesARequestToDeleteBookingWithBasicAuth(String username, String password) {
        Response response = RequestSpecBuilderFactory.createAuthenticatedRequestSpec()
                .auth().preemptive().basic(username, password)
                .pathParam("bookingID", scenarioState.getData("bookingID", String.class))
                .when().delete(scenarioState.getData("endpoint", String.class) + "/{bookingID}");
        scenarioState.setResponse(response);
    }
}
