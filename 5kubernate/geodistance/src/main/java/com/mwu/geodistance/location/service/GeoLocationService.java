package com.mwu.geodistance.location.service;


import com.mwu.geodistance.common.model.CustomPage;
import com.mwu.geodistance.location.model.VehicleDistance;
import com.mwu.geodistance.location.model.VehicleLocation;
import com.mwu.geodistance.location.model.dto.request.FindNearestByMemberRequest;
import com.mwu.geodistance.location.model.dto.request.FindNearestByPointRequest;
import com.mwu.geodistance.location.model.dto.request.VehicleLocationRequest;

import java.util.Optional;

public interface GeoLocationService {

    VehicleLocation add(VehicleLocationRequest request);

    Optional<VehicleLocation> findByName(String vehicleName);

    CustomPage<VehicleLocation> findNearestVehicles(FindNearestByPointRequest request);

    CustomPage<VehicleLocation> findNearestVehiclesByMember(FindNearestByMemberRequest request);

    VehicleDistance distanceBetweenVehiclesKm(String vehicleA, String vehicleB);

}
