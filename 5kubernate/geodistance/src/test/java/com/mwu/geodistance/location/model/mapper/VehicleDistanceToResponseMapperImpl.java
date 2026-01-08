package com.mwu.geodistance.location.model.mapper;

import com.mwu.geodistance.location.model.VehicleDistance;
import com.mwu.geodistance.location.model.dto.response.VehicleDistanceResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VehicleDistanceToResponseMapperImpl implements VehicleDistanceToResponseMapper {
    @Override
    public VehicleDistanceResponse map(VehicleDistance d) {
        if (d == null) return null;
        return VehicleDistanceResponse.builder()
                .vehicleA(d.getVehicleA())
                .vehicleB(d.getVehicleB())
                .distanceKm(d.getDistanceKm())
                .build();
    }

    @Override
    public List<VehicleDistanceResponse> map(Collection<VehicleDistance> sources) {
        if (sources == null) return List.of();
        if (sources.isEmpty()) return List.of();
        List<VehicleDistanceResponse> res = new ArrayList<>();
        for (VehicleDistance source : sources) {
            res.add(map(source));
        }
        return res;
    }
}
