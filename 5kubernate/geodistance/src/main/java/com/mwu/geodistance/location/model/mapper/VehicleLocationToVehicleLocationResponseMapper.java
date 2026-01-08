package com.mwu.geodistance.location.model.mapper;


import com.mwu.geodistance.common.model.mapper.BaseMapper;
import com.mwu.geodistance.location.model.VehicleLocation;
import com.mwu.geodistance.location.model.dto.response.VehicleLocationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VehicleLocationToVehicleLocationResponseMapper extends BaseMapper<VehicleLocation, VehicleLocationResponse> {

    @Named("mapToResponse")
    default VehicleLocationResponse mapToResponse(VehicleLocation source) {
        if (source == null) return null;

        String distanceStr = source.getDistanceKm() == null
                ? null
                : (source.getDistanceKm() + " km");

        return VehicleLocationResponse.builder()
                .vehicleName(source.getVehicleName())
                .averageDistance(distanceStr)
                .point(source.getPoint())
                .hash(source.getHash())
                .build();
    }

    // 根据 VehicleLocationToVehicleLocationResponseMapper 接口的定义，动态生成一个实现类，并返回其实例。MapStruct 会在编译时生成该实现类，确保类型安全和高效的映射逻辑
    static VehicleLocationToVehicleLocationResponseMapper initialize() {
        return Mappers.getMapper(VehicleLocationToVehicleLocationResponseMapper.class);
    }
}

