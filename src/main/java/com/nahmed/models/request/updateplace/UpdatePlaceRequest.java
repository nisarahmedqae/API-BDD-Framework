package com.nahmed.models.request.updateplace;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlaceRequest {
    private String place_id;
    private String address;
    private String key;
}