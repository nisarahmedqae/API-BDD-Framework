package com.nahmed.stepdefinitions;

import static org.testng.Assert.*;

import java.util.Map;

import com.nahmed.builders.RequestSpecBuilderFactory;
import com.nahmed.utils.TestContext;
import io.restassured.response.Response;
import org.json.JSONObject;

import com.nahmed.models.response.BookingDTO;
import com.nahmed.utils.ResponseHandler;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateBookingStepdefinition {

    private static final Logger LOG = LoggerFactory.getLogger(CreateBookingStepdefinition.class);
    private final TestContext testContext;
    private final RequestSpecBuilderFactory requestSpecFactory;

    public CreateBookingStepdefinition(TestContext testContext, RequestSpecBuilderFactory requestSpecFactory) {
        this.testContext = testContext;
        this.requestSpecFactory = requestSpecFactory;
    }

    @When("user creates a booking")
    public void userCreatesABooking(DataTable dataTable) {
        Map<String, String> bookingData = dataTable.asMaps().get(0);
        JSONObject bookingBody = new JSONObject();
        bookingBody.put("firstname", bookingData.get("firstname"));
        bookingBody.put("lastname", bookingData.get("lastname"));
        bookingBody.put("totalprice", Integer.valueOf(bookingData.get("totalprice")));
        bookingBody.put("depositpaid", Boolean.valueOf(bookingData.get("depositpaid")));
        JSONObject bookingDates = new JSONObject();
        bookingDates.put("checkin", (bookingData.get("checkin")));
        bookingDates.put("checkout", (bookingData.get("checkout")));
        bookingBody.put("bookingdates", bookingDates);
        bookingBody.put("additionalneeds", bookingData.get("additionalneeds"));
        Response response = requestSpecFactory.createRequestSpec()
                .body(bookingBody.toString())
                .when().post(testContext.getData("endpoint", String.class));
        testContext.setResponse(response);
        BookingDTO bookingDTO = ResponseHandler.deserializedResponse(response, BookingDTO.class);
        assertNotNull(bookingDTO, "Booking not created");
        LOG.info("Newly created booking ID: " + bookingDTO.getBookingid());
        testContext.setData("bookingID", bookingDTO.getBookingid());
        validateBookingData(new JSONObject(bookingData), bookingDTO);
    }

    private void validateBookingData(JSONObject bookingData, BookingDTO bookingDTO) {
        LOG.info("Validating booking data: {}", bookingData.toString());
        assertNotNull(bookingDTO.getBookingid(), "Booking ID missing");
        assertEquals(bookingDTO.getBooking().getFirstname(), bookingData.get("firstname"), "First Name did not match");
        assertEquals(bookingDTO.getBooking().getLastname(), bookingData.get("lastname"), "Last Name did not match");
        assertEquals(bookingDTO.getBooking().getTotalprice(), bookingData.get("totalprice"), "Total Price did not match");
        assertEquals(bookingDTO.getBooking().getDepositpaid(), bookingData.get("depositpaid"), "Deposit Paid did not match");
        assertEquals(bookingDTO.getBooking().getAdditionalneeds(), bookingData.get("additionalneeds"), "Additional Needs did not match");
        assertEquals(bookingDTO.getBooking().getBookingdates().getCheckin(), bookingData.get("checkin"), "Check in Date did not match");
        assertEquals(bookingDTO.getBooking().getBookingdates().getCheckout(), bookingData.get("checkout"), "Check out Date did not match");
    }

}
