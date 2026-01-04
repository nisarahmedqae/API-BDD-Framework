package com.nahmed.stepdefinitions;

import com.nahmed.factories.RequestSpecBuilderFactory;
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

import java.util.Map;

public class GetPlace_Stepdefs {

    private static final Logger LOG = LoggerFactory.getLogger(GetPlace_Stepdefs.class);
    private final TestContext testContext;
    private final RequestSpecBuilderFactory requestSpecFactory;

    public GetPlace_Stepdefs(TestContext testContext, RequestSpecBuilderFactory requestSpecFactory) {
        this.testContext = testContext;
        this.requestSpecFactory = requestSpecFactory;
    }

    @When("the user sends a GET request to the {string} endpoint")
    public void theUserSendsAGETRequestToTheEndpoint(String endpoint) {
        // Retrieve the place_id saved by the AddPlace step
        String place_id = testContext.getData("place_id", String.class);

        // Run API
        Response response = requestSpecFactory.createRequestSpec()
                .queryParam("key", "qaclick123")
                .queryParam("place_id", place_id)
                .when().get(endpoint);

        // Setup for other APIs
        testContext.setResponse(response);
    }

    @And("user validates the get place success response with the following data:")
    public void userValidatesTheGetPlaceSuccessResponseWithTheFollowingData(DataTable dataTable) {
        Map<String, String> expectedData = dataTable.asMap(String.class, String.class);

        // 1. Deserialize response to POJO
        GetPlaceResponse getPlaceResponse = ResponseHandler.deserializedResponse(
                testContext.getResponse(), GetPlaceResponse.class);

        // 2. Validate Nested Location Object
        Assert.assertEquals(getPlaceResponse.getLocation().getLatitude(),
                expectedData.get("latitude"), "Latitude mismatch");
        Assert.assertEquals(getPlaceResponse.getLocation().getLongitude(),
                expectedData.get("longitude"), "Longitude mismatch");

        // 3. Validate Basic String Fields
        Assert.assertEquals(getPlaceResponse.getName(), expectedData.get("name"));
        Assert.assertEquals(getPlaceResponse.getPhone_number(), expectedData.get("phone_number"));
        Assert.assertEquals(getPlaceResponse.getAddress(), expectedData.get("address"));
        Assert.assertEquals(getPlaceResponse.getWebsite(), expectedData.get("website"));
        Assert.assertEquals(getPlaceResponse.getLanguage(), expectedData.get("language"));

        // 4. Validate Numeric Accuracy
        Assert.assertEquals(getPlaceResponse.getAccuracy(), expectedData.get("accuracy"));

        // 5. Validate Types
        Assert.assertEquals(getPlaceResponse.getTypes(), expectedData.get("types"));

        LOG.info("All fields validated successfully for GET Place API");
    }
}
