package com.nahmed.stepdefinitions;

import static org.testng.Assert.*;

import com.nahmed.factories.RequestSpecBuilderFactory;
import com.nahmed.utils.TestContext;
import com.nahmed.utils.ValidationUtils;

import com.nahmed.models.response.BookingDetailsDTO;
import com.nahmed.models.request.BookingID;
import com.nahmed.utils.ResponseHandler;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewBookingDetailsStepdefinition {

    private static final Logger LOG = LoggerFactory.getLogger(ViewBookingDetailsStepdefinition.class);
    private final TestContext testContext;
    private final RequestSpecBuilderFactory requestSpecFactory;

    public ViewBookingDetailsStepdefinition(TestContext testContext, RequestSpecBuilderFactory requestSpecFactory) {
        this.testContext = testContext;
        this.requestSpecFactory = requestSpecFactory;
    }

    @Given("user has access to endpoint {string}")
    public void userHasAccessToEndpoint(String endpoint) {
        testContext.setData("endpoint", endpoint);
    }

    @When("user makes a request to view booking IDs")
    public void userMakesARequestToViewBookingIDs() {
        Response response = requestSpecFactory.createRequestSpec()
                .when().get(testContext.getData("endpoint", String.class));
        testContext.setResponse(response);
        int bookingID = response.getBody().jsonPath().getInt("[0].bookingid");
        LOG.info("Booking ID: " + bookingID);
        assertNotNull(bookingID, "Booking ID not found!");
        testContext.setData("bookingID", bookingID);
    }

    @Then("user should get the response code {int}")
    public void userShpuldGetTheResponseCode(Integer statusCode) {
        assertEquals(Long.valueOf(statusCode), Long.valueOf(testContext.getResponse().getStatusCode()));
    }


    @Then("user should see all the booking IDs")
    public void userShouldSeeAllTheBookingIDS() {
        BookingID[] bookingIDs = ResponseHandler.deserializedResponse(testContext.getResponse(), BookingID[].class);
        assertNotNull(bookingIDs, "Booking ID not found!!");
    }

    @Then("user makes a request to view details of a booking ID")
    public void userMakesARequestToViewDetailsOfBookingID() {
        Integer bookingID = testContext.getData("bookingID", Integer.class);
        LOG.info("Session BookingID: " + bookingID);
        Response response = requestSpecFactory.createRequestSpec().pathParam("bookingID", bookingID)
                .when().get(testContext.getData("endpoint", String.class) + "/{bookingID}");
        testContext.setResponse(response);
        BookingDetailsDTO bookingDetails = ResponseHandler.deserializedResponse(response, BookingDetailsDTO.class);
        assertNotNull(bookingDetails, "Booking Details not found!!");
        testContext.setData("firstname", bookingDetails.getFirstname());
        testContext.setData("lastname", bookingDetails.getLastname());
    }

    @Given("user makes a request to view booking IDs from {string} to {string}")
    public void userMakesARequestToViewBookingFromTo(String checkin, String checkout) {
        Response response = requestSpecFactory.createRequestSpec()
                .queryParams("checkin", checkin, "checkout", checkout)
                .when().get(testContext.getData("endpoint", String.class));
        testContext.setResponse(response);
    }

    @Then("user makes a request to view all the booking IDs of that user name")
    public void userMakesARequestToViewBookingIDByUserName() {
        String firstname = testContext.getData("firstname", String.class);
        String lastname = testContext.getData("lastname", String.class);
        LOG.info("Session firstname: " + firstname);
        LOG.info("Session lastname: " + lastname);
        Response response = requestSpecFactory.createRequestSpec()
                .queryParams("firstname", firstname, "lastname", lastname)
                .when().get(testContext.getData("endpoint", String.class));
        testContext.setResponse(response);
        BookingID[] bookingIDs = ResponseHandler.deserializedResponse(response, BookingID[].class);
        assertNotNull(bookingIDs, "Booking ID not found!!");
    }

    @Then("user validates the response with JSON schema {string}")
    public void userValidatesResponseWithJSONSchema(String schemaFileName) {
        ValidationUtils.validateResponseAgainstSchema(testContext.getResponse(), schemaFileName);
    }

    @When("user makes a request to check the health of booking service")
    public void userMakesARequestToCheckTheHealthOfBookingService() {
        Response response = requestSpecFactory.createRequestSpec()
                .get(testContext.getData("endpoint", String.class));
        testContext.setResponse(response);
    }

}
