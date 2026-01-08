package com.mwu.geodistance.location.model.mapper;

import com.mwu.geodistance.location.model.VehicleLocation;
import com.mwu.geodistance.location.model.dto.response.VehicleLocationResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VehicleLocationToVehicleLocationResponseMapperImpl implements VehicleLocationToVehicleLocationResponseMapper {
    @Override
    public VehicleLocationResponse map(VehicleLocation source) {
        if (source == null) return null;

        String distanceStr = source.getDistanceKm() == null
                ? null
                : (source.getDistanceKm() + " km");

        return VehicleLocationResponse.builder()
                .vehicleName(source.getVehicleName())
                .averageDistance(distanceStr)
                .point(source.getPoint())
                .hash(source.getHash())
                .build();
    }

    @Override
    public List<VehicleLocationResponse> map(Collection<VehicleLocation> sources) {
        if (sources == null) return List.of();
        if (sources.isEmpty()) return List.of();
        List<VehicleLocationResponse> res = new ArrayList<>();
        for (VehicleLocation source : sources) {
            res.add(map(source));
        }

        return res;
    }
}
