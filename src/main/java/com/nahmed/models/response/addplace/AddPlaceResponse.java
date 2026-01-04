package com.nahmed.models.response.addplace;

import lombok.Data;

@Data
public class AddPlaceResponse {
    private String status;
    private String place_id;
    private String scope;
    private String reference;
    private String id;
}
