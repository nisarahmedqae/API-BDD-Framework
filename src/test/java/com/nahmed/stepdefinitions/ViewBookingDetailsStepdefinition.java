package com.nahmed.stepdefinitions;

import static org.testng.Assert.*;

import com.nahmed.utils.ValidationUtils;

import com.nahmed.models.response.BookingDetailsDTO;
import com.nahmed.models.request.BookingID;
import com.nahmed.utils.ResponseHandler;
import com.nahmed.utils.TestContext;

import io.cucumber.java.en.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewBookingDetailsStepdefinition {

    private TestContext testContext;
    private static final Logger LOG = LoggerFactory.getLogger(ViewBookingDetailsStepdefinition.class);

    public ViewBookingDetailsStepdefinition(TestContext testContext) {
        this.testContext = testContext;
    }

    @Given("user has access to endpoint {string}")
    public void userHasAccessToEndpoint(String endpoint) {
        testContext.sessionMap.put("endpoint", endpoint);
    }

    @When("user makes a request to view booking IDs")
    public void userMakesARequestToViewBookingIDs() {
        testContext.response = testContext.requestSetup().when().get(testContext.sessionMap.get("endpoint").toString());
        int bookingID = testContext.response.getBody().jsonPath().getInt("[0].bookingid");
        LOG.info("Booking ID: " + bookingID);
        assertNotNull(bookingID, "Booking ID not found!");
        testContext.sessionMap.put("bookingID", bookingID);
    }

    @Then("user should get the response code {int}")
    public void userShpuldGetTheResponseCode(Integer statusCode) {
        assertEquals(Long.valueOf(statusCode), Long.valueOf(testContext.response.getStatusCode()));
    }

    @Then("user should see all the booking IDs")
    public void userShouldSeeAllTheBookingIDS() {
        BookingID[] bookingIDs = ResponseHandler.deserializedResponse(testContext.response, BookingID[].class);
        assertNotNull(bookingIDs, "Booking ID not found!!");
    }

    @Then("user makes a request to view details of a booking ID")
    public void userMakesARequestToViewDetailsOfBookingID() {
        LOG.info("Session BookingID: " + testContext.sessionMap.get("bookingID"));
        testContext.response = testContext.requestSetup().pathParam("bookingID", testContext.sessionMap.get("bookingID"))
                .when().get(testContext.sessionMap.get("endpoint") + "/{bookingID}");
        BookingDetailsDTO bookingDetails = ResponseHandler.deserializedResponse(testContext.response, BookingDetailsDTO.class);
        assertNotNull(bookingDetails, "Booking Details not found!!");
        testContext.sessionMap.put("firstname", bookingDetails.getFirstname());
        testContext.sessionMap.put("lastname", bookingDetails.getLastname());
    }

    @Given("user makes a request to view booking IDs from {string} to {string}")
    public void userMakesARequestToViewBookingFromTo(String checkin, String checkout) {
        testContext.response = testContext.requestSetup()
                .queryParams("checkin", checkin, "checkout", checkout)
                .when().get(testContext.sessionMap.get("endpoint").toString());
    }

    @Then("user makes a request to view all the booking IDs of that user name")
    public void userMakesARequestToViewBookingIDByUserName() {
        LOG.info("Session firstname: " + testContext.sessionMap.get("firstname"));
        LOG.info("Session lastname: " + testContext.sessionMap.get("lastname"));
        testContext.response = testContext.requestSetup()
                .queryParams("firstname", testContext.sessionMap.get("firstname"), "lastname", testContext.sessionMap.get("lastname"))
                .when().get(testContext.sessionMap.get("endpoint").toString());
        BookingID[] bookingIDs = ResponseHandler.deserializedResponse(testContext.response, BookingID[].class);
        assertNotNull(bookingIDs, "Booking ID not found!!");
    }

    @Then("user validates the response with JSON schema {string}")
    public void userValidatesResponseWithJSONSchema(String schemaFileName) {
        ValidationUtils.validateResponseAgainstSchema(testContext.response, schemaFileName);
    }

    @When("user makes a request to check the health of booking service")
    public void userMakesARequestToCheckTheHealthOfBookingService() {
        testContext.response = testContext.requestSetup().get(testContext.sessionMap.get("endpoint").toString());
    }
}
