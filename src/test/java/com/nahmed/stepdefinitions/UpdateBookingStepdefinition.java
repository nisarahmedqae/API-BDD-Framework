package com.nahmed.stepdefinitions;

import static org.testng.Assert.*;

import java.util.Map;

import org.json.JSONObject;

import com.nahmed.models.response.BookingDetailsDTO;
import com.nahmed.utils.ResponseHandler;
import com.nahmed.utils.TestContext;

import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateBookingStepdefinition {

	private TestContext testContext;
	private static final Logger LOG = LoggerFactory.getLogger(UpdateBookingStepdefinition.class);
	
	public UpdateBookingStepdefinition(TestContext testContext) {
		this.testContext = testContext;
	}

	@When("user creates a auth token with credential {string} & {string}")
	public void userCreatesAAuthTokenWithCredential(String username, String password) {
		JSONObject credentials = new JSONObject();
		credentials.put("username", username);
		credentials.put("password", password);
		testContext.response = testContext.requestSetup().body(credentials.toString())
				.when().post(testContext.sessionMap.get("endpoint").toString());
		String token = testContext.response.path("token");
		LOG.info("Auth Token: "+token);
		testContext.sessionMap.put("token", "token="+token);
	}

	@When("user updates the details of a booking")
	public void userUpdatesABooking(DataTable dataTable) {
		Map<String,String> bookingData = dataTable.asMaps().get(0);
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

		testContext.response = testContext.requestSetup()
				.header("Cookie", testContext.sessionMap.get("token").toString())
				.pathParam("bookingID", testContext.sessionMap.get("bookingID"))
				.body(bookingBody.toString())
				.when().put(testContext.sessionMap.get("endpoint")+"/{bookingID}");

		BookingDetailsDTO bookingDetailsDTO = ResponseHandler.deserializedResponse(testContext.response, BookingDetailsDTO.class);
		assertNotNull(bookingDetailsDTO,"Booking not created");
	}

	
	@When("user makes a request to update first name {string} & Last name {string}")
	public void userMakesARequestToUpdateFirstNameLastName(String firstName, String lastName) {
		JSONObject body = new JSONObject();
		body.put("firstname", firstName);
		body.put("lastname", lastName);

		testContext.response = testContext.requestSetup()
				.header("Cookie", testContext.sessionMap.get("token").toString())
				.pathParam("bookingID", testContext.sessionMap.get("bookingID"))
				.body(body.toString())
				.when().patch(testContext.sessionMap.get("endpoint")+"/{bookingID}");
		
		BookingDetailsDTO bookingDetailsDTO = ResponseHandler.deserializedResponse(testContext.response, BookingDetailsDTO.class);
		assertNotNull(bookingDetailsDTO, "Booking not created");
		assertEquals(bookingDetailsDTO.getFirstname(),firstName, "First Name did not match");
		assertEquals(bookingDetailsDTO.getLastname(), lastName, "Last Name did not match");
	}
}
