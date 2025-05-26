package com.nahmed.cucumber.stepdefs;

import static org.testng.Assert.*;

import java.util.Map;

import org.json.JSONObject;

import com.nahmed.cucumber.models.response.BookingDTO;
import com.nahmed.cucumber.utils.ResponseHandler;
import com.nahmed.cucumber.utils.TestContext;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateBookingStepdefinition {
    private TestContext context;
    private static final Logger LOG = LoggerFactory.getLogger(CreateBookingStepdefinition.class);

    public CreateBookingStepdefinition(TestContext context) {
        this.context = context;
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

        context.response = context.requestSetup().body(bookingBody.toString())
                .when().post(context.session.get("endpoint").toString());

        BookingDTO bookingDTO = ResponseHandler.deserializedResponse(context.response, BookingDTO.class);
        assertNotNull(bookingDTO, "Booking not created");
        LOG.info("Newly created booking ID: " + bookingDTO.getBookingid());
        context.session.put("bookingID", bookingDTO.getBookingid());
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
