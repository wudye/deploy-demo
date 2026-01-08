package com.mwu.geodistance.location.model.mapper;


import com.mwu.geodistance.common.model.mapper.BaseMapper;
import com.mwu.geodistance.location.model.VehicleDistance;
import com.mwu.geodistance.location.model.dto.response.VehicleDistanceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VehicleDistanceToResponseMapper extends BaseMapper<VehicleDistance, VehicleDistanceResponse> {

    @Named("mapToResponse")
    default VehicleDistanceResponse mapToResponse(VehicleDistance d) {
        if (d == null) return null;
        return VehicleDistanceResponse.builder()
                .vehicleA(d.getVehicleA())
                .vehicleB(d.getVehicleB())
                .distanceKm(d.getDistanceKm())
                .build();
    }

    static VehicleDistanceToResponseMapper initialize() {
        return Mappers.getMapper(VehicleDistanceToResponseMapper.class);
    }

}
