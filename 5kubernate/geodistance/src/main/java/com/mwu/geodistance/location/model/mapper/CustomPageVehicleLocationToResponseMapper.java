package com.mwu.geodistance.location.model.mapper;

import com.mwu.geodistance.common.model.CustomPage;
import com.mwu.geodistance.location.model.VehicleLocation;
import com.mwu.geodistance.location.model.dto.response.VehicleLocationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CustomPageVehicleLocationToResponseMapper {

    VehicleLocationToVehicleLocationResponseMapper locationMapper =
            VehicleLocationToVehicleLocationResponseMapper.initialize();

    @Named("mapToResponsePage")
    default CustomPage<VehicleLocationResponse> mapToResponsePage(CustomPage<VehicleLocation> domainPage) {
        if (domainPage == null) {
            return null;
        }

        List<VehicleLocationResponse> mapped = domainPage.getContent() == null
                ? List.of()
                : domainPage.getContent().stream()
                .map(locationMapper::mapToResponse)
                .toList();

        return CustomPage.<VehicleLocationResponse>builder()
                .content(mapped)
                .pageNumber(domainPage.getPageNumber())
                .pageSize(domainPage.getPageSize())
                .totalElementCount(domainPage.getTotalElementCount())
                .totalPageCount(domainPage.getTotalPageCount())
                .build();
    }

    static CustomPageVehicleLocationToResponseMapper initialize() {
        return Mappers.getMapper(CustomPageVehicleLocationToResponseMapper.class);
    }
}

