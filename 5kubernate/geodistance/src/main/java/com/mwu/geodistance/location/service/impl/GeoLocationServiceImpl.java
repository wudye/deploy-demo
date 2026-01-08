package com.mwu.geodistance.location.service.impl;

import com.mwu.geodistance.common.exception.ApiException;
import com.mwu.geodistance.common.model.CustomPage;
import com.mwu.geodistance.common.model.dto.request.CustomPagingRequest;
import com.mwu.geodistance.location.exception.GeoSearchFailedException;
import com.mwu.geodistance.location.exception.VehicleAlreadyExistsException;
import com.mwu.geodistance.location.exception.VehicleNotFoundException;
import com.mwu.geodistance.location.model.VehicleDistance;
import com.mwu.geodistance.location.model.VehicleLocation;
import com.mwu.geodistance.location.model.dto.request.FindNearestByMemberRequest;
import com.mwu.geodistance.location.model.dto.request.FindNearestByPointRequest;
import com.mwu.geodistance.location.model.dto.request.VehicleLocationRequest;
import com.mwu.geodistance.location.model.mapper.GeoResultsToCustomPageMapper;
import com.mwu.geodistance.location.service.GeoLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.mwu.geodistance.location.utils.Constants.MAX_RESULT_WINDOW;
import static com.mwu.geodistance.location.utils.Constants.VEHICLE_LOCATION_KEY;


@Service
@RequiredArgsConstructor
public class GeoLocationServiceImpl implements GeoLocationService {

    private final GeoOperations<String, String> geoOperations;

    private final GeoResultsToCustomPageMapper geoResultsToCustomPageMapper =
            GeoResultsToCustomPageMapper.initialize();

    @Override
    public VehicleLocation add(VehicleLocationRequest request) {

        try {

            Optional<VehicleLocation> existing = findByName(request.getVehicleName());

            if (existing.isPresent()) {
                throw new VehicleAlreadyExistsException(request.getVehicleName());
            }

            Point point = new Point(request.getLongitude(), request.getLatitude());
            geoOperations.add(VEHICLE_LOCATION_KEY, point, request.getVehicleName());

            String hash = geoOperations.hash(VEHICLE_LOCATION_KEY, request.getVehicleName())
                    .stream()
                    .findFirst()
                    .map(Object::toString)
                    .orElse(null);

            return VehicleLocation.builder()
                    .vehicleName(request.getVehicleName())
                    .distanceKm(null)
                    .point(point)
                    .hash(hash)
                    .build();

        } catch (ApiException ex) {
            throw ex;
        } catch (DataAccessException ex) {
            throw new GeoSearchFailedException(mostSpecificMessage(ex));
        } catch (Exception ex) {
            throw new GeoSearchFailedException(ex.getMessage());
        }
    }

    @Override
    public Optional<VehicleLocation> findByName(String vehicleName) {

        try {
            List<Point> points = geoOperations.position(VEHICLE_LOCATION_KEY, vehicleName);

            if (points == null || points.isEmpty() || points.getFirst() == null) {
                return Optional.empty();
            }

            Point point = points.getFirst();
            String hash = geoOperations.hash(VEHICLE_LOCATION_KEY, vehicleName)
                    .stream()
                    .findFirst()
                    .map(Object::toString)
                    .orElse(null);

            return Optional.of(VehicleLocation.builder()
                    .vehicleName(vehicleName)
                    .distanceKm(null)
                    .point(point)
                    .hash(hash)
                    .build());

        } catch (ApiException ex) {
            throw ex;
        } catch (DataAccessException ex) {
            throw new GeoSearchFailedException(mostSpecificMessage(ex));
        } catch (Exception ex) {
            throw new GeoSearchFailedException(ex.getMessage());
        }
    }

    @Override
    public CustomPage<VehicleLocation> findNearestVehicles(FindNearestByPointRequest request) {

        Pageable pageable = request.toPageable();

        Circle circle = new Circle(
                new Point(request.getLongitude(), request.getLatitude()),
                new Distance(request.getKm(), Metrics.KILOMETERS)
        );

        RedisGeoCommands.GeoRadiusCommandArgs pageArgs = geoArgsFromSorting(request)
                .includeCoordinates()
                .includeDistance()
                .limit(safeEndIndex(pageable));

        var pagedResults = geoOperations.radius(VEHICLE_LOCATION_KEY, circle, pageArgs);

        var countResults = geoOperations.radius(
                VEHICLE_LOCATION_KEY,
                circle,
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
        );

        long total = (countResults == null || countResults.getContent() == null)
                ? 0
                : countResults.getContent().size();

        return geoResultsToCustomPageMapper.mapToCustomPage(
                pagedResults, pageable, total,
                VEHICLE_LOCATION_KEY, null, geoOperations
        );

    }

    @Override
    public CustomPage<VehicleLocation> findNearestVehiclesByMember(FindNearestByMemberRequest request) {

        Pageable pageable = request.toPageable();

        if (findByName(request.getCenterVehicleName()).isEmpty()) {
            throw new VehicleNotFoundException(request.getCenterVehicleName());
        }

        RedisGeoCommands.GeoRadiusCommandArgs pageArgs = geoArgsFromSorting(request)
                .includeCoordinates()
                .includeDistance()
                .limit(safeEndIndex(pageable));

        var pagedResults = geoOperations.radius(
                VEHICLE_LOCATION_KEY,
                request.getCenterVehicleName(),
                new Distance(request.getKm(), Metrics.KILOMETERS),
                pageArgs
        );

        var countResults = geoOperations.radius(
                VEHICLE_LOCATION_KEY,
                request.getCenterVehicleName(),
                new Distance(request.getKm(), Metrics.KILOMETERS),
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
        );

        long total = (countResults == null || countResults.getContent() == null)
                ? 0
                : countResults.getContent().size();

        if (total > 0 && countResults != null && countResults.getContent() != null) {
            boolean centerIncluded = countResults.getContent().stream()
                    .anyMatch(r -> r != null && r.getContent() != null
                            && request.getCenterVehicleName().equals(r.getContent().getName()));
            if (centerIncluded) total -= 1;
        }

        return geoResultsToCustomPageMapper.mapToCustomPage(
                pagedResults, pageable, total,
                VEHICLE_LOCATION_KEY, request.getCenterVehicleName(), geoOperations
        );

    }

    public VehicleDistance distanceBetweenVehiclesKm(String vehicleA, String vehicleB) {

        if (findByName(vehicleA).isEmpty()) {
            throw new VehicleNotFoundException(vehicleA);
        }

        if (findByName(vehicleB).isEmpty()) {
            throw new VehicleNotFoundException(vehicleB);
        }

        Distance d = geoOperations.distance(VEHICLE_LOCATION_KEY, vehicleA, vehicleB, Metrics.KILOMETERS);

        if (d == null) {
            throw new GeoSearchFailedException(vehicleA + ", " + vehicleB);
        }

        return VehicleDistance.builder()
                .vehicleA(vehicleA)
                .vehicleB(vehicleB)
                .distanceKm(d.getValue())
                .build();

    }

    private int safeEndIndex(Pageable pageable) {
        long endExclusive = (long) (pageable.getPageNumber() + 1) * pageable.getPageSize();
        long safe = Math.min(endExclusive, MAX_RESULT_WINDOW);
        return Math.toIntExact(safe);
    }

    private RedisGeoCommands.GeoRadiusCommandArgs geoArgsFromSorting(CustomPagingRequest req) {
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs();

        if (req.getSorting() != null
                && "distance".equalsIgnoreCase(req.getSorting().getSortBy())
                && "DESC".equalsIgnoreCase(req.getSorting().getSortDirection())) {
            return args.sortDescending();
        }
        return args.sortAscending();
    }

    private String mostSpecificMessage(DataAccessException ex) {
        Throwable c = ex.getMostSpecificCause();
        return c.getMessage() != null ? c.getMessage() : ex.getMessage();
    }

}
