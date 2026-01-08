package com.mwu.geodistance.location.model.mapper;


import com.mwu.geodistance.location.model.VehicleDistance;
import com.mwu.geodistance.location.model.dto.response.VehicleDistanceResponse;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class VehicleDistanceToResponseMapperTest {

    private final VehicleDistanceToResponseMapper mapper = new VehicleDistanceToResponseMapperImpl();

    @Test
    void map_whenSourceIsNull_returnsNull() {
        // When & Then
        VehicleDistanceResponse result = mapper.map((VehicleDistance) null);

        assertThat(result).isNull();
    }

    @Test
    void map_whenSourceIsNotNull_mapsAllFields() {

        // Given
        VehicleDistance source = VehicleDistance.builder()
                .vehicleA("CAR_A")
                .vehicleB("CAR_B")
                .distanceKm(12.34)
                .build();

        // When & Then
        VehicleDistanceResponse result = mapper.map(source);

        assertThat(result).isNotNull();
        assertThat(result.getVehicleA()).isEqualTo("CAR_A");
        assertThat(result.getVehicleB()).isEqualTo("CAR_B");
        assertThat(result.getDistanceKm()).isEqualTo(12.34);

    }

    @Test
    void mapCollection_whenSourcesIsNull_returnsEmptyList() {
        // When & Then
        List<VehicleDistanceResponse> result = mapper.map((Collection<VehicleDistance>) null);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    void mapCollection_whenSourcesIsEmpty_returnsEmptyList() {

        // Given
        Collection<VehicleDistance> sources = List.of();

        // When
        List<VehicleDistanceResponse> result = mapper.map(sources);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

    }

    @Test
    void mapCollection_whenSourcesHasItems_mapsEachItem_includingNullElements() {

        // Given
        VehicleDistance d1 = VehicleDistance.builder()
                .vehicleA("V1")
                .vehicleB("V2")
                .distanceKm(1.0)
                .build();

        VehicleDistance d2 = VehicleDistance.builder()
                .vehicleA("V3")
                .vehicleB("V4")
                .distanceKm(2.5)
                .build();

        Collection<VehicleDistance> sources = Arrays.asList(d1, null, d2);

        // When & Then
        List<VehicleDistanceResponse> result = mapper.map(sources);

        assertThat(result).hasSize(3);

        assertThat(result.getFirst()).isNotNull();
        assertThat(result.getFirst().getVehicleA()).isEqualTo("V1");
        assertThat(result.getFirst().getVehicleB()).isEqualTo("V2");
        assertThat(result.getFirst().getDistanceKm()).isEqualTo(1.0);

        assertThat(result.get(1)).isNull();

        assertThat(result.get(2)).isNotNull();
        assertThat(result.get(2).getVehicleA()).isEqualTo("V3");
        assertThat(result.get(2).getVehicleB()).isEqualTo("V4");
        assertThat(result.get(2).getDistanceKm()).isEqualTo(2.5);

    }
}
