package com.nahmed.stepdefinitions;

import com.nahmed.factories.RequestSpecBuilderFactory;
import com.nahmed.models.request.deleteplace.DeletePlaceRequest;
import com.nahmed.models.request.updateplace.UpdatePlaceRequest;
import com.nahmed.models.response.deleteplace.DeletePlaceErrorResponse;
import com.nahmed.models.response.deleteplace.DeletePlaceResponse;
import com.nahmed.models.response.updateplace.UpdatePlaceErrorResponse;
import com.nahmed.models.response.updateplace.UpdatePlaceResponse;
import com.nahmed.utils.ResponseHandler;
import com.nahmed.utils.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.util.Map;

public class UpdatePlace_Stepdefs {

    private static final Logger LOG = LoggerFactory.getLogger(UpdatePlace_Stepdefs.class);
    private final TestContext testContext;
    private final RequestSpecBuilderFactory requestSpecFactory;

    public UpdatePlace_Stepdefs(TestContext testContext, RequestSpecBuilderFactory requestSpecFactory) {
        this.testContext = testContext;
        this.requestSpecFactory = requestSpecFactory;
    }

    @When("the user sends a PUT request to the {string} endpoint with the following body:")
    public void theUserSendsAPUTRequestToTheEndpointWithTheFollowingBody(String endpoint, DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);

        // Retrieve the place_id saved by the AddPlace step
        String place_id = testContext.getData("place_id", String.class);

        UpdatePlaceRequest updatePlaceRequest = UpdatePlaceRequest.builder()
                .place_id(place_id)
                .address(data.get("address"))
                .key(data.get("key"))
                .build();

        // Run API
        Response response = requestSpecFactory.createRequestSpec()
                .queryParam("key", "qaclick123").body(updatePlaceRequest)
                .when().put(endpoint);

        // Setup for other APIs
        testContext.setResponse(response);
    }

    @And("user validates the update place success response with the following data:")
    public void userValidatesTheUpdatePlaceSuccessResponseWithTheFollowingData(DataTable dataTable) {
        Map<String, String> expectedData = dataTable.asMap(String.class, String.class);

        // 1. Deserialize response to POJO
        UpdatePlaceResponse updatePlaceResponse = ResponseHandler.deserializedResponse(
                testContext.getResponse(), UpdatePlaceResponse.class);

        Assert.assertEquals(updatePlaceResponse.getMsg(), expectedData.get("msg"));
    }

    @When("the user sends a PUT request to the {string} endpoint with invalid request body:")
    public void theUserSendsAPUTRequestToTheMapsApiPlaceUpdateJsonEndpointWithInvalidBody(String endpoint, DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);

        UpdatePlaceRequest updatePlaceRequest = UpdatePlaceRequest.builder()
                .place_id(data.get("place_id"))
                .address(data.get("address"))
                .key(data.get("key"))
                .build();

        // Run API
        Response response = requestSpecFactory.createRequestSpec()
                .queryParam("key", "qaclick123").body(updatePlaceRequest)
                .when().put(endpoint);

        // Setup for other APIs
        testContext.setResponse(response);
    }

    @And("user validates the update place error response with the following data:")
    public void userValidatesTheUpdatePlaceErrorResponseWithTheFollowingData(DataTable dataTable) {
        Map<String, String> expectedData = dataTable.asMap(String.class, String.class);

        // 1. Deserialize response to POJO
        UpdatePlaceErrorResponse updatePlaceErrorResponse = ResponseHandler.deserializedResponse(
                testContext.getResponse(), UpdatePlaceErrorResponse.class);

        Assert.assertEquals(updatePlaceErrorResponse.getMsg(), expectedData.get("msg"));
    }
}
