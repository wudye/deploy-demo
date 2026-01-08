package com.mwu.geodistance.location.controller;

import com.mwu.geodistance.common.model.CustomPage;
import com.mwu.geodistance.common.model.dto.response.CustomResponse;
import com.mwu.geodistance.location.model.VehicleDistance;
import com.mwu.geodistance.location.model.VehicleLocation;
import com.mwu.geodistance.location.model.dto.request.FindNearestByMemberRequest;
import com.mwu.geodistance.location.model.dto.request.FindNearestByPointRequest;
import com.mwu.geodistance.location.model.dto.request.VehicleDistanceRequest;
import com.mwu.geodistance.location.model.dto.request.VehicleLocationRequest;
import com.mwu.geodistance.location.model.dto.response.VehicleDistanceResponse;
import com.mwu.geodistance.location.model.dto.response.VehicleLocationResponse;
import com.mwu.geodistance.location.model.mapper.CustomPageVehicleLocationToResponseMapper;
import com.mwu.geodistance.location.model.mapper.VehicleDistanceToResponseMapper;
import com.mwu.geodistance.location.model.mapper.VehicleLocationToVehicleLocationResponseMapper;
import com.mwu.geodistance.location.service.GeoLocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vehicles")
public class GeoLocationController {

    private final GeoLocationService geoLocationService;

    private static final VehicleLocationToVehicleLocationResponseMapper locationToVehicleLocationResponseMapper =
            VehicleLocationToVehicleLocationResponseMapper.initialize();

    private static final CustomPageVehicleLocationToResponseMapper customPageVehicleLocationToResponseMapper =
            CustomPageVehicleLocationToResponseMapper.initialize();

    private static final VehicleDistanceToResponseMapper vehicleDistanceToResponseMapper =  VehicleDistanceToResponseMapper.initialize();

    @PostMapping("/location")
    public ResponseEntity<CustomResponse<VehicleLocationResponse>> add(@Valid @RequestBody VehicleLocationRequest request) {
        VehicleLocation domain = geoLocationService.add(request);
        VehicleLocationResponse response = locationToVehicleLocationResponseMapper.mapToResponse(domain);
        return ResponseEntity.ok(CustomResponse.successOf(response));
    }

    @PostMapping("/nearest/point")
    public ResponseEntity<CustomResponse<CustomPage<VehicleLocationResponse>>> nearestByPoint(
            @Valid @RequestBody FindNearestByPointRequest request) {
        CustomPage<VehicleLocation> domainPage = geoLocationService.findNearestVehicles(request);
        return ResponseEntity.ok(CustomResponse.successOf(customPageVehicleLocationToResponseMapper.mapToResponsePage(domainPage)));
    }

    @PostMapping("/nearest/member")
    public ResponseEntity<CustomResponse<CustomPage<VehicleLocationResponse>>> nearestByMember(
            @Valid @RequestBody FindNearestByMemberRequest request) {
        CustomPage<VehicleLocation> domainPage = geoLocationService.findNearestVehiclesByMember(request);
        return ResponseEntity.ok(CustomResponse.successOf(customPageVehicleLocationToResponseMapper.mapToResponsePage(domainPage)));
    }

    @PostMapping("/distance")
    public ResponseEntity<CustomResponse<VehicleDistanceResponse>> distanceBetweenVehicles(
            @Valid @RequestBody VehicleDistanceRequest request) {
        VehicleDistance domain = geoLocationService.distanceBetweenVehiclesKm(
                request.getVehicleA(),
                request.getVehicleB()
        );
        VehicleDistanceResponse response = vehicleDistanceToResponseMapper.mapToResponse(domain);
        return ResponseEntity.ok(CustomResponse.successOf(response));
    }

}
