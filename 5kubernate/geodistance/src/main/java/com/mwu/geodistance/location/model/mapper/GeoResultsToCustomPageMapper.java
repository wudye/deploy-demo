package com.mwu.geodistance.location.model.mapper;


import com.mwu.geodistance.common.model.CustomPage;
import com.mwu.geodistance.location.model.VehicleLocation;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;

import java.util.List;
import java.util.Objects;

@Mapper
public interface GeoResultsToCustomPageMapper {

    GeoResultToVehicleLocationMapper geoResultMapper =
            GeoResultToVehicleLocationMapper.initialize();

    @Named("mapToCustomPage")
    default CustomPage<VehicleLocation> mapToCustomPage(
            GeoResults<RedisGeoCommands.GeoLocation<String>> limitedResults,
            Pageable pageable,
            long totalElementCount,
            String key,
            String centerMemberOrNull,
            GeoOperations<String, String> geoOperations
    ) {
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> raw =
                (limitedResults == null || limitedResults.getContent() == null)
                        ? List.of()
                        : limitedResults.getContent();

        if (raw.isEmpty()) {
            Page<VehicleLocation> empty = new PageImpl<>(List.of(), pageable, totalElementCount);
            return CustomPage.of(empty.getContent(), empty);
        }

        // Exclude center member for BYMEMBER results
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> filtered = raw;
        if (centerMemberOrNull != null && !centerMemberOrNull.isBlank()) {
            filtered = raw.stream()
                    .filter(r -> r != null && r.getContent() != null)
                    .filter(r -> !centerMemberOrNull.equals(r.getContent().getName()))
                    .toList();
        }

        List<VehicleLocation> fetched = filtered.stream()
                .map(r -> geoResultMapper.mapFromGeoResult(r, key, centerMemberOrNull, geoOperations))
                .filter(Objects::nonNull)
                .toList();

        // Local OFFSET slice
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), fetched.size());

        List<VehicleLocation> pageContent =
                (start >= fetched.size()) ? List.of() : fetched.subList(start, end);

        Page<VehicleLocation> page = new PageImpl<>(pageContent, pageable, totalElementCount);
        return CustomPage.of(page.getContent(), page);
    }

    static GeoResultsToCustomPageMapper initialize() {
        return Mappers.getMapper(GeoResultsToCustomPageMapper.class);
    }

}
