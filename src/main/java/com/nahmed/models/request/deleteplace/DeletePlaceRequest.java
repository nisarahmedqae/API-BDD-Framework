package com.nahmed.models.request.deleteplace;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeletePlaceRequest {
    private String place_id;
}