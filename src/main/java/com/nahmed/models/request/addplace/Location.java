package com.nahmed.models.request.addplace;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Location {
    private double lat;
    private double lng;
}