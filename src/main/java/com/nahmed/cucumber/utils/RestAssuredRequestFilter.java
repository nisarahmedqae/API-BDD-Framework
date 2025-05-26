package com.nahmed.cucumber.utils;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestAssuredRequestFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(RestAssuredRequestFilter.class);

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);
        if (LOG.isDebugEnabled()) { // Optional check for slight performance gain
            LOG.debug("-----------------------------------------------------------------------------------------");
            LOG.debug(" Request Method => {}", requestSpec.getMethod());
            LOG.debug(" Request URI => {}", requestSpec.getURI());
            LOG.debug(" Request Header =>\n{}", requestSpec.getHeaders());
            LOG.debug(" Request Body => {}", requestSpec.getBody() != null ? requestSpec.getBody().toString() : "[No Body]"); // Handle null body
            LOG.debug("\n Response Status => {}", response.getStatusLine());
            LOG.debug(" Response Header =>\n{}", response.getHeaders());
            LOG.debug(" Response Body => \n{}", response.getBody().prettyPrint());
            LOG.debug("-----------------------------------------------------------------------------------------");
        }
        return response;
    }
}