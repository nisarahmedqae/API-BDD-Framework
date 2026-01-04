package com.nahmed.factories;

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

    // ANSI Escape Codes for colors
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_CYAN = "\u001B[36m";

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);

        LOG.info(ANSI_CYAN + "-----------------------------------------------------------------------------------------" + ANSI_RESET);

        LOG.info(ANSI_CYAN + " Request Method => {}" + ANSI_RESET, requestSpec.getMethod());
        ExtentLogger.info("Request Method => " + requestSpec.getMethod());

        LOG.info(ANSI_CYAN + " Request URI => {}" + ANSI_RESET, requestSpec.getURI());
        ExtentLogger.info("Request URI => " + requestSpec.getURI());

        LOG.info(ANSI_CYAN + " Request Headers =>\n{}" + ANSI_RESET, requestSpec.getHeaders());
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
        LOG.info(ANSI_CYAN + " Request Body => \n{}" + ANSI_RESET, prettyRequestBody);
        ExtentLogger.infoInJSON(prettyRequestBody);

        LOG.info(ANSI_CYAN + "\n Response Status => {}" + ANSI_RESET, response.getStatusLine());
        ExtentLogger.info(response.getStatusLine());

        LOG.info(" Response Body => \n{}" + ANSI_RESET, response.asPrettyString());
        ExtentLogger.infoInJSON(response.asPrettyString());

        LOG.info(ANSI_CYAN + "-----------------------------------------------------------------------------------------" + ANSI_RESET);

        return response;
    }
}