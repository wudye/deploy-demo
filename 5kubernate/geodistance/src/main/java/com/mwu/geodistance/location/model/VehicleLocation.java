package com.mwu.geodistance.location.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleLocation {

    private String vehicleName;

    private Double distanceKm;

    private Point point;

    private String hash;
}

