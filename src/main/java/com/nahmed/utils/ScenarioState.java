package com.nahmed.utils;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

public class ScenarioState {

    private RequestSpecification request;
    private Response response;
    private final Map<String, Object> data = new HashMap<>();

    // Getters and Setters
    public RequestSpecification getRequest() {
        return request;
    }

    public void setRequest(RequestSpecification request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public <T> T getData(String key, Class<T> type) {
        return type.cast(data.get(key));
    }

    public void setData(String key, Object value) {
        data.put(key, value);
    }

}