package com.nahmed.listeners;

import com.nahmed.reports.ExtentLogger;
import com.nahmed.utils.RequestHandler;
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

        LOG.info("------------------------------------------------------------------");

        LOG.info("Request Method => {}", requestSpec.getMethod());
        ExtentLogger.info("Request Method => " + requestSpec.getMethod());

        LOG.info("Request URI => {}", requestSpec.getURI());
        ExtentLogger.info("Request URI => " + requestSpec.getURI());

        LOG.info("Request Headers =>\n{}", requestSpec.getHeaders());
        var headerList = requestSpec.getHeaders().asList();
        if (!headerList.isEmpty()) {
            String[][] headerTable = headerList.stream()
                    .map(h -> new String[]{h.getName(), h.getValue()})
                    .toArray(String[][]::new);
            ExtentLogger.infoInTable(headerTable);
        } else {
            ExtentLogger.info("Request Headers: [None]");
        }

        Object rawRequestBody = requestSpec.getBody();
        String prettyRequestBody = rawRequestBody != null ? RequestHandler.prettyPrint(rawRequestBody.toString()) : "[No Request Body]";
        LOG.info("Request Body => \n{}", prettyRequestBody);
        ExtentLogger.infoInJSON(prettyRequestBody);

        LOG.info("\n Response Status => {}", response.getStatusLine());
        ExtentLogger.info(response.getStatusLine());

        LOG.info("Response Body => \n{}", response.asPrettyString());
        ExtentLogger.infoInJSON(response.asPrettyString());

        LOG.info("------------------------------------------------------------------");

        return response;
    }
}