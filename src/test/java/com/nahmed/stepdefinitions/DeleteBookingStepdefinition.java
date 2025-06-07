package com.nahmed.stepdefinitions;

import com.nahmed.utils.TestContext;

import io.cucumber.java.en.When;

public class DeleteBookingStepdefinition {
	private TestContext testContext;

	public DeleteBookingStepdefinition(TestContext testContext) {
		this.testContext = testContext;
	}

	@When("user makes a request to delete booking with basic auth {string} & {string}")
	public void userMakesARequestToDeleteBookingWithBasicAuth(String username, String password) {
		testContext.response = testContext.requestSetup()
				.auth().preemptive().basic(username, password)
				.pathParam("bookingID", testContext.sessionMap.get("bookingID"))
				.when().delete(testContext.sessionMap.get("endpoint")+"/{bookingID}");
	}
}
