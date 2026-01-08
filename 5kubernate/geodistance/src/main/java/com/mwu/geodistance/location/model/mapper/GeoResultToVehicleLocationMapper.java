package com.mwu.geodistance.location.model.mapper;

import com.mwu.geodistance.location.model.VehicleLocation;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;

@Mapper
public interface GeoResultToVehicleLocationMapper {

    @Named("mapFromGeoResult")
    default VehicleLocation mapFromGeoResult(
            GeoResult<RedisGeoCommands.GeoLocation<String>> result,
            String key,
            String centerMemberOrNull,
            GeoOperations<String, String> geoOps
    ) {
        if (result == null || result.getContent() == null) return null;

        String name = result.getContent().getName();
        Point point = result.getContent().getPoint();

        // Prefer distance returned by radius query
        Double distanceKm = (result.getDistance() != null) ? result.getDistance().getValue() : null;

        // If BYMEMBER and distance missing -> compute via GEODIST
        if (distanceKm == null
                && centerMemberOrNull != null && !centerMemberOrNull.isBlank()
                && !centerMemberOrNull.equals(name)) {
            Distance d = geoOps.distance(key, centerMemberOrNull, name, Metrics.KILOMETERS);
            distanceKm = (d != null) ? d.getValue() : null;
        }

        String hash = geoOps.hash(key, name)
                .stream()
                .findFirst()
                .map(Object::toString)
                .orElse(null);

        return VehicleLocation.builder()
                .vehicleName(name)
                .distanceKm(distanceKm)
                .point(point)
                .hash(hash)
                .build();
    }

    static GeoResultToVehicleLocationMapper initialize() {
        return Mappers.getMapper(GeoResultToVehicleLocationMapper.class);
    }

}
