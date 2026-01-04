package com.nahmed.models.response.getplace;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor  // Necessary for Jackson to create the object
@AllArgsConstructor // Necessary for Lombok Builder to work with NoArgsConstructor
public class Location {
    private String latitude;
    private String longitude;
}