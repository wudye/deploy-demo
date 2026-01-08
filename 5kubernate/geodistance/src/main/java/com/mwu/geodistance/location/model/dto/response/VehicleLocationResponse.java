package com.mwu.geodistance.location.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VehicleLocationResponse {

    private String vehicleName;

    /**
     * Example: "0.57 km"
     */
    private String averageDistance;

    /**
     * Redis returns x=longitude, y=latitude
     */
    private Point point;

    /**
     * Redis GEOHASH string (if you include it)
     */
    private String hash;

}

