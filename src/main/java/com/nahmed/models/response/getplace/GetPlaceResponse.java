package com.nahmed.models.response.getplace;

import lombok.Data;

@Data
public class GetPlaceResponse {
    private Location location;
    private String accuracy;
    private String name;
    private String phone_number;
    private String address;
    private String types;
    private String website;
    private String language;
}
