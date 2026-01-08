package com.mwu.geodistance.location.model.mapper;

import com.mwu.geodistance.location.model.VehicleLocation;
import org.junit.jupiter.api.Test;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GeoResultToVehicleLocationMapperTest {

    private final GeoResultToVehicleLocationMapper mapper =
            GeoResultToVehicleLocationMapper.initialize();

    @Test
    void mapFromGeoResult_whenResultIsNull_returnsNull() {

        // Given
        @SuppressWarnings("unchecked")
        GeoOperations<String, String> geoOps = mock(GeoOperations.class);

        // When & Then
        VehicleLocation mapped = mapper.mapFromGeoResult(
                null,
                "vehicle_location",
                "CENTER",
                geoOps
        );

        assertThat(mapped).isNull();
        verifyNoInteractions(geoOps);

    }

    @Test
    void mapFromGeoResult_whenDistanceMissingAndByMember_shouldComputeViaGeoDist() {

        // Given
        String key = "vehicle_location";
        String center = "CENTER";
        String name = "CAR_1";
        Point point = new Point(29.0, 41.0);

        @SuppressWarnings("unchecked")
        GeoResult<RedisGeoCommands.GeoLocation<String>> result = mock(GeoResult.class);

        RedisGeoCommands.GeoLocation<String> content = new RedisGeoCommands.GeoLocation<>(name, point);

        // When
        when(result.getContent()).thenReturn(content);
        when(result.getDistance()).thenReturn(null);

        @SuppressWarnings("unchecked")
        GeoOperations<String, String> geoOps = mock(GeoOperations.class);
        when(geoOps.distance(key, center, name, Metrics.KILOMETERS))
                .thenReturn(new Distance(7.5, Metrics.KILOMETERS));
        when(geoOps.hash(key, name)).thenReturn(List.of("HASH123"));

        // Then
        VehicleLocation mapped = mapper.mapFromGeoResult(result, key, center, geoOps);

        assertThat(mapped).isNotNull();
        assertThat(mapped.getVehicleName()).isEqualTo(name);
        assertThat(mapped.getPoint()).isEqualTo(point);
        assertThat(mapped.getDistanceKm()).isEqualTo(7.5);
        assertThat(mapped.getHash()).isEqualTo("HASH123");

        // Verify
        verify(geoOps).distance(key, center, name, Metrics.KILOMETERS);
        verify(geoOps).hash(key, name);
        verifyNoMoreInteractions(geoOps);

    }

}
