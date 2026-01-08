package com.mwu.geodistance.location.model.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VehicleDistanceResponse {

    private String vehicleA;
    private String vehicleB;
    private Double distanceKm;

}
