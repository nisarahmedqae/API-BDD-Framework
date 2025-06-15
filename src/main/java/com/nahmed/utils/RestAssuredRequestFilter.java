package com.nahmed.utils;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestAssuredRequestFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(RestAssuredRequestFilter.class);

    // ANSI Escape Codes for colors
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_CYAN = "\u001B[36m";

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);
        if (LOG.isDebugEnabled()) {
            LOG.debug(ANSI_CYAN + "-----------------------------------------------------------------------------------------" + ANSI_RESET);
            LOG.debug(ANSI_CYAN + " Request Method => {}" + ANSI_RESET, requestSpec.getMethod());
            LOG.debug(ANSI_CYAN + " Request URI => {}" + ANSI_RESET, requestSpec.getURI());
            LOG.debug(ANSI_CYAN + " Request Header =>\n{}" + ANSI_RESET, requestSpec.getHeaders());
            LOG.debug(ANSI_CYAN + " Request Body => \n{}" + ANSI_RESET, requestSpec.getBody() != null ? requestSpec.getBody().toString() : "[No Body]"); // Handle null body
            LOG.debug(ANSI_CYAN + "\n Response Status => {}" + ANSI_RESET, response.getStatusLine());
            //LOG.debug(ANSI_CYAN + " Response Header =>\n{}" + ANSI_RESET, response.getHeaders());
            //LOG.debug(ANSI_CYAN + " Response Body => \n{}" + ANSI_RESET, response.getBody().prettyPrint());
            LOG.debug(ANSI_CYAN + "-----------------------------------------------------------------------------------------" + ANSI_RESET);
        }
        return response;
    }
}