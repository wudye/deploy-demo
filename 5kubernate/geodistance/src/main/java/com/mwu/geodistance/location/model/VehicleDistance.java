package com.mwu.geodistance.location.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VehicleDistance {

    private String vehicleA;
    private String vehicleB;
    private Double distanceKm;
}
