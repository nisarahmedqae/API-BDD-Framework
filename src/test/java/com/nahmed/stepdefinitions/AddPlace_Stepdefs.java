package com.nahmed.stepdefinitions;

import com.nahmed.factories.RequestSpecBuilderFactory;
import com.nahmed.models.request.addplace.AddPlace;
import com.nahmed.models.request.addplace.Location;
import com.nahmed.models.response.addplace.AddPlaceResponse;
import com.nahmed.utils.AssertionService;
import com.nahmed.utils.ResponseHandler;
import com.nahmed.utils.TestContext;
import com.nahmed.utils.ValidationUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.util.Arrays;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class AddPlace_Stepdefs {

    private static final Logger LOG = LoggerFactory.getLogger(AddPlace_Stepdefs.class);
    private final TestContext testContext;
    private final RequestSpecBuilderFactory requestSpecFactory;

    public AddPlace_Stepdefs(TestContext testContext, RequestSpecBuilderFactory requestSpecFactory) {
        this.testContext = testContext;
        this.requestSpecFactory = requestSpecFactory;
    }

    @When("the user sends a POST request to the {string} endpoint with the following body:")
    public void theUserSendsAPOSTRequestToTheEndpointWithTheFollowingBody(String endpoint, DataTable dataTable) {
        // 1. Convert DataTable to a Map
        Map<String, String> data = dataTable.asMap(String.class, String.class);

        // 2. Build Location Object using Lombok Builder
        Location loc = Location.builder()
                .lat(Double.parseDouble(data.get("lat")))
                .lng(Double.parseDouble(data.get("lng")))
                .build();

        // 3. Build AddPlace Object (Parent)
        AddPlace addPlace = AddPlace.builder()
                .accuracy(Integer.parseInt(data.get("accuracy")))
                .name(data.get("name"))
                .phone_number(data.get("phone_number"))
                .address(data.get("address"))
                .website(data.get("website"))
                .language(data.get("language"))
                .location(loc)
                .types(Arrays.asList(data.get("types").split(", "))) // Splits "shoe park, shop" into a List
                .build();

        // Run API
        Response response = requestSpecFactory.createRequestSpec()
                .queryParam("key", "qaclick123").body(addPlace)
                .when().post(endpoint);

        // Setup for other APIs
        testContext.setResponse(response);

        String place_id = response.jsonPath().getString("place_id") != null
                ? response.jsonPath().getString("place_id") : "Blank place_id";
        testContext.setData("place_id", place_id);
    }

    @Then("user should get the response code {int}")
    public void userShpuldGetTheResponseCode(int statusCode) {
        assertEquals(testContext.getResponse().getStatusCode(), statusCode);
    }

    @And("user validates the add place success response with the following data:")
    public void userValidatesTheAddPlaceSuccessResponseWithTheFollowingData(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);

        AddPlaceResponse addPlaceResponse = ResponseHandler.deserializedResponse(testContext.getResponse(), AddPlaceResponse.class);

        // JSON
        Assert.assertEquals(addPlaceResponse.getStatus(), data.get("status"));
        Assert.assertEquals(addPlaceResponse.getScope(), data.get("scope"));

        AssertionService.assertStringLength(addPlaceResponse.getPlace_id(), 32);
        AssertionService.assertStringLength(addPlaceResponse.getReference(), 64);
        AssertionService.assertStringLength(addPlaceResponse.getId(), 32);
    }

    @And("user validates the response against the JSON schema {string}")
    public void userValidatesTheResponseAgainstTheJSONSchema(String schemaFileName) {
        ValidationUtils.validateResponseAgainstSchema(testContext.getResponse(), schemaFileName);
    }


}
