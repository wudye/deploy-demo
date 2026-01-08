package com.mwu.geodistance.location.model.mapper;

import com.mwu.geodistance.common.model.CustomPage;
import com.mwu.geodistance.location.model.dto.response.VehicleLocationResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CustomPageVehicleLocationToResponseMapperTest {

    private final CustomPageVehicleLocationToResponseMapper mapper =
            CustomPageVehicleLocationToResponseMapper.initialize();

    @Test
    void mapToResponsePage_whenDomainPageIsNull_returnsNull() {
        // When & Then
        CustomPage<VehicleLocationResponse> result = mapper.mapToResponsePage(null);

        assertThat(result).isNull();
    }

}