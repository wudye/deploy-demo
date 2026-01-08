package com.mwu.geodistance.location.model.mapper;


import com.mwu.geodistance.common.model.CustomPage;
import com.mwu.geodistance.location.model.VehicleLocation;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GeoResultsToCustomPageMapperTest {

    private final GeoResultsToCustomPageMapper mapper =
            GeoResultsToCustomPageMapper.initialize();

    @Test
    void mapToCustomPage_whenLimitedResultsIsNull_returnsEmptyCustomPage() {

        // Given
        Pageable pageable = PageRequest.of(0, 10);
        long total = 123L;

        @SuppressWarnings("unchecked")
        GeoOperations<String, String> geoOps = mock(GeoOperations.class);

        // When & Then
        CustomPage<VehicleLocation> result = mapper.mapToCustomPage(
                null,
                pageable,
                total,
                "vehicle_location",
                null,
                geoOps
        );

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getPageNumber()).isEqualTo(pageable.getPageNumber() + 1);
        assertThat(result.getPageSize()).isEqualTo(pageable.getPageSize());
        assertThat(result.getTotalElementCount()).isEqualTo(total);

    }

    @Test
    void mapToCustomPage_whenLimitedResultsContentIsNull_returnsEmptyCustomPage() {

        // Given
        Pageable pageable = PageRequest.of(0, 5);
        long total = 10L;

        @SuppressWarnings("unchecked")
        GeoOperations<String, String> geoOps = mock(GeoOperations.class);

        @SuppressWarnings("unchecked")
        GeoResults<RedisGeoCommands.GeoLocation<String>> limitedResults =
                mock(GeoResults.class);

        // When
        when(limitedResults.getContent()).thenReturn(null);

        // Then
        CustomPage<VehicleLocation> result = mapper.mapToCustomPage(
                limitedResults,
                pageable,
                total,
                "vehicle_location",
                null,
                geoOps
        );

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getPageNumber()).isEqualTo(pageable.getPageNumber() + 1);
        assertThat(result.getPageSize()).isEqualTo(pageable.getPageSize());
        assertThat(result.getTotalElementCount()).isEqualTo(total);

    }

}