package com.mwu.geodistance.location.model.mapper;


import com.mwu.geodistance.location.model.VehicleLocation;
import com.mwu.geodistance.location.model.dto.response.VehicleLocationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.data.geo.Point;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class VehicleLocationToVehicleLocationResponseMapperTest {

    private final VehicleLocationToVehicleLocationResponseMapper mapper =
            new VehicleLocationToVehicleLocationResponseMapperImpl();

    @Test
    void map_whenSourceIsNull_returnsNull() {
        // when
        VehicleLocationResponse result = mapper.map((VehicleLocation) null);

        // then
        assertThat(result).isNull();
    }

    @Test
    void map_whenSourceIsNotNull_mapsAllFields() {

        // Given
        Point point = new Point(29.0, 41.0);

        VehicleLocation source = VehicleLocation.builder()
                .vehicleName("CAR_1")
                .point(point)
                .hash("HASH123")
                .distanceKm(7.5)
                .build();

        // When & Then
        VehicleLocationResponse result = mapper.map(source);

        assertThat(result).isNotNull();
        assertThat(result.getVehicleName()).isEqualTo("CAR_1");
        assertThat(result.getPoint()).isEqualTo(point);
        assertThat(result.getHash()).isEqualTo("HASH123");

    }

    @Test
    void mapCollection_whenSourcesIsNull_returnsEmptyList() {
        // When & Then
        List<VehicleLocationResponse> result = mapper.map((Collection<VehicleLocation>) null);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    void mapCollection_whenSourcesIsEmpty_returnsEmptyList() {

        // Given
        Collection<VehicleLocation> sources = List.of();

        // When & Then
        List<VehicleLocationResponse> result = mapper.map(sources);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

    }

    @Test
    void mapCollection_whenSourcesHasItems_mapsEachItem_includingNullElements() {

        // Given
        VehicleLocation v1 = VehicleLocation.builder()
                .vehicleName("V1")
                .point(new Point(10.0, 20.0))
                .hash("H1")
                .build();

        VehicleLocation v2 = VehicleLocation.builder()
                .vehicleName("V2")
                .point(new Point(30.0, 40.0))
                .hash("H2")
                .build();

        Collection<VehicleLocation> sources = Arrays.asList(v1, null, v2);

        // When & Then
        List<VehicleLocationResponse> result = mapper.map(sources);

        assertThat(result).hasSize(3);

        assertThat(result.getFirst()).isNotNull();
        assertThat(result.getFirst().getVehicleName()).isEqualTo("V1");
        assertThat(result.getFirst().getPoint()).isEqualTo(new Point(10.0, 20.0));
        assertThat(result.getFirst().getHash()).isEqualTo("H1");

        assertThat(result.get(1)).isNull();

        assertThat(result.get(2)).isNotNull();
        assertThat(result.get(2).getVehicleName()).isEqualTo("V2");
        assertThat(result.get(2).getPoint()).isEqualTo(new Point(30.0, 40.0));
        assertThat(result.get(2).getHash()).isEqualTo("H2");

    }
}
