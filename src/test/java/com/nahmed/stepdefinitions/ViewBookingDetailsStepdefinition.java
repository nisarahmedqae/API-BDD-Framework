package com.nahmed.stepdefinitions;

import static org.testng.Assert.*;

import com.nahmed.builders.RequestSpecBuilderFactory;
import com.nahmed.utils.ScenarioState;
import com.nahmed.utils.ValidationUtils;

import com.nahmed.models.response.BookingDetailsDTO;
import com.nahmed.models.request.BookingID;
import com.nahmed.utils.ResponseHandler;
import com.nahmed.utils.TestContext;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewBookingDetailsStepdefinition {

    private final ScenarioState scenarioState;
    private static final Logger LOG = LoggerFactory.getLogger(ViewBookingDetailsStepdefinition.class);

    public ViewBookingDetailsStepdefinition(ScenarioState scenarioState) {
        this.scenarioState = scenarioState;
    }

    @Given("user has access to endpoint {string}")
    public void userHasAccessToEndpoint(String endpoint) {
        scenarioState.setData("endpoint", endpoint);
    }
/*
    @When("user makes a request to view booking IDs")
    public void userMakesARequestToViewBookingIDs() {
        Response response = RequestSpecBuilderFactory.createAuthenticatedRequestSpec()
                .when().get(scenarioState.getData("endpoint", String.class));
        scenarioState.setResponse(response);
        int bookingID = response.getBody().jsonPath().getInt("[0].bookingid");
        LOG.info("Booking ID: " + bookingID);
        assertNotNull(bookingID, "Booking ID not found!");
        //TestContext.getInstance().set("bookingID", bookingID);
    }

    @Then("user should get the response code {int}")
    public void userShpuldGetTheResponseCode(Integer statusCode) {
        assertEquals(Long.valueOf(statusCode), Long.valueOf(scenarioState.getResponse().getStatusCode()));
    }

    @Then("user should see all the booking IDs")
    public void userShouldSeeAllTheBookingIDS() {
        BookingID[] bookingIDs = ResponseHandler.deserializedResponse(scenarioState.getResponse(), BookingID[].class);
        assertNotNull(bookingIDs, "Booking ID not found!!");
    }

    @Then("user makes a request to view details of a booking ID")
    public void userMakesARequestToViewDetailsOfBookingID() {
        Integer bookingID = TestContext.getInstance().get("bookingID", Integer.class);
        LOG.info("Session BookingID: " + bookingID);
        Response response = RequestSpecBuilderFactory.createAuthenticatedRequestSpec().pathParam("bookingID", bookingID)
                .when().get(scenarioState.getData("endpoint", String.class) + "/{bookingID}");
        scenarioState.setResponse(response);
        BookingDetailsDTO bookingDetails = ResponseHandler.deserializedResponse(response, BookingDetailsDTO.class);
        assertNotNull(bookingDetails, "Booking Details not found!!");
        TestContext.getInstance().set("firstname", bookingDetails.getFirstname());
        TestContext.getInstance().set("lastname", bookingDetails.getLastname());
    }

    @Given("user makes a request to view booking IDs from {string} to {string}")
    public void userMakesARequestToViewBookingFromTo(String checkin, String checkout) {
        Response response = RequestSpecBuilderFactory.createAuthenticatedRequestSpec()
                .queryParams("checkin", checkin, "checkout", checkout)
                .when().get(scenarioState.getData("endpoint", String.class));
        scenarioState.setResponse(response);
    }

    @Then("user makes a request to view all the booking IDs of that user name")
    public void userMakesARequestToViewBookingIDByUserName() {
        String firstname = TestContext.getInstance().get("firstname", String.class);
        String lastname = TestContext.getInstance().get("lastname", String.class);
        LOG.info("Session firstname: " + firstname);
        LOG.info("Session lastname: " + lastname);
        Response response = RequestSpecBuilderFactory.createAuthenticatedRequestSpec()
                .queryParams("firstname", firstname, "lastname", lastname)
                .when().get(scenarioState.getData("endpoint", String.class));
        scenarioState.setResponse(response);
        BookingID[] bookingIDs = ResponseHandler.deserializedResponse(response, BookingID[].class);
        assertNotNull(bookingIDs, "Booking ID not found!!");
    }

    @Then("user validates the response with JSON schema {string}")
    public void userValidatesResponseWithJSONSchema(String schemaFileName) {
        ValidationUtils.validateResponseAgainstSchema(scenarioState.getResponse(), schemaFileName);
    }

    @When("user makes a request to check the health of booking service")
    public void userMakesARequestToCheckTheHealthOfBookingService() {
        Response response = RequestSpecBuilderFactory.createAuthenticatedRequestSpec()
                .get(scenarioState.getData("endpoint", String.class));
    }

 */
}
