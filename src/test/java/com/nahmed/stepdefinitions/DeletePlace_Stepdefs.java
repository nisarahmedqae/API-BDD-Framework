package com.nahmed.stepdefinitions;

import com.nahmed.factories.RequestSpecBuilderFactory;
import com.nahmed.models.request.addplace.AddPlace;
import com.nahmed.models.request.addplace.Location;
import com.nahmed.models.request.deleteplace.DeletePlaceRequest;
import com.nahmed.models.response.deleteplace.DeletePlaceErrorResponse;
import com.nahmed.models.response.deleteplace.DeletePlaceResponse;
import com.nahmed.models.response.getplace.GetPlaceResponse;
import com.nahmed.utils.ResponseHandler;
import com.nahmed.utils.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.util.Arrays;
import java.util.Map;

public class DeletePlace_Stepdefs {

    private static final Logger LOG = LoggerFactory.getLogger(DeletePlace_Stepdefs.class);
    private final TestContext testContext;
    private final RequestSpecBuilderFactory requestSpecFactory;

    public DeletePlace_Stepdefs(TestContext testContext, RequestSpecBuilderFactory requestSpecFactory) {
        this.testContext = testContext;
        this.requestSpecFactory = requestSpecFactory;
    }

    @When("the user sends a DELETE request to the {string} endpoint")
    public void theUserSendsADELETERequestToTheEndpoint(String endpoint) {
        // Retrieve the place_id saved by the AddPlace step
        String place_id = testContext.getData("place_id", String.class);

        DeletePlaceRequest deletePlaceRequest = DeletePlaceRequest.builder()
                .place_id(place_id)
                .build();

        // Run API
        Response response = requestSpecFactory.createRequestSpec()
                .queryParam("key", "qaclick123").body(deletePlaceRequest)
                .when().delete(endpoint);

        // Setup for other APIs
        testContext.setResponse(response);
    }

    @And("user validates the delete place success response with the following data:")
    public void userValidatesTheDeletePlaceSuccessResponseWithTheFollowingData(DataTable dataTable) {
        Map<String, String> expectedData = dataTable.asMap(String.class, String.class);

        // 1. Deserialize response to POJO
        DeletePlaceResponse deletePlaceResponse = ResponseHandler.deserializedResponse(
                testContext.getResponse(), DeletePlaceResponse.class);

        Assert.assertEquals(deletePlaceResponse.getStatus(), expectedData.get("status"));
    }

    @When("the user sends a DELETE request to the {string} endpoint with invalid body")
    public void theUserSendsADELETERequestToTheEndpointWithInvalidBody(String endpoint) {
        // Retrieve the place_id saved by the AddPlace step
        String place_id = "invalid place_id";

        DeletePlaceRequest deletePlaceRequest = DeletePlaceRequest.builder()
                .place_id(place_id)
                .build();

        // Run API
        Response response = requestSpecFactory.createRequestSpec()
                .queryParam("key", "qaclick123").body(deletePlaceRequest)
                .when().delete(endpoint);

        // Setup for other APIs
        testContext.setResponse(response);
    }

    @And("user validates the delete place error response with the following data:")
    public void userValidatesTheDeletePlaceErrorResponseWithTheFollowingData(DataTable dataTable) {
        Map<String, String> expectedData = dataTable.asMap(String.class, String.class);

        // 1. Deserialize response to POJO
        DeletePlaceErrorResponse deletePlaceErrorResponse = ResponseHandler.deserializedResponse(
                testContext.getResponse(), DeletePlaceErrorResponse.class);

        Assert.assertEquals(deletePlaceErrorResponse.getMsg(), expectedData.get("msg"));
    }
}
