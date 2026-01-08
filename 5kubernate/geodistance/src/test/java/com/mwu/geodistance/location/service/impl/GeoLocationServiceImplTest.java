package com.mwu.geodistance.location.service.impl;


import com.mwu.geodistance.base.AbstractBaseServiceTest;
import com.mwu.geodistance.common.exception.error.CustomError;
import com.mwu.geodistance.common.model.CustomPage;
import com.mwu.geodistance.common.model.CustomPaging;
import com.mwu.geodistance.common.model.CustomSorting;
import com.mwu.geodistance.location.exception.GeoSearchFailedException;
import com.mwu.geodistance.location.exception.VehicleAlreadyExistsException;
import com.mwu.geodistance.location.exception.VehicleNotFoundException;
import com.mwu.geodistance.location.model.VehicleDistance;
import com.mwu.geodistance.location.model.VehicleLocation;
import com.mwu.geodistance.location.model.dto.request.FindNearestByMemberRequest;
import com.mwu.geodistance.location.model.dto.request.FindNearestByPointRequest;
import com.mwu.geodistance.location.model.dto.request.VehicleLocationRequest;
import com.mwu.geodistance.location.model.mapper.GeoResultsToCustomPageMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class GeoLocationServiceImplTest extends AbstractBaseServiceTest {

    private static final String KEY = "vehicle_location";

    @InjectMocks
    private GeoLocationServiceImpl service;

    @Mock
    private GeoOperations<String, String> geoOperations;

    private final GeoResultsToCustomPageMapper geoResultsToCustomPageMapper =
            GeoResultsToCustomPageMapper.initialize();

    private static final Distance ZERO_KM = new Distance(0.0, Metrics.KILOMETERS);

    @Test
    void add_shouldAddNewVehicle_whenNotExists() {

        // Given
        VehicleLocationRequest req = VehicleLocationRequest.builder()
                .vehicleName("car_new")
                .latitude(41.0)
                .longitude(29.0)
                .build();

        // When
        when(geoOperations.position(anyString(), eq("car_new")))
                .thenReturn(List.of());
        when(geoOperations.add(anyString(), any(Point.class), eq("car_new")))
                .thenReturn(1L);
        when(geoOperations.hash(anyString(), eq("car_new")))
                .thenReturn(List.of("sxk9xxxx"));

        // Then
        VehicleLocation out = service.add(req);

        assertNotNull(out);
        assertEquals("car_new", out.getVehicleName());
        assertNotNull(out.getPoint());
        assertEquals(29.0, out.getPoint().getX());
        assertEquals(41.0, out.getPoint().getY());
        assertEquals("sxk9xxxx", out.getHash());

        // Verify
        verify(geoOperations).position(anyString(), eq("car_new"));
        verify(geoOperations).add(anyString(), any(Point.class), eq("car_new"));
        verify(geoOperations).hash(anyString(), eq("car_new"));

    }

    @Test
    void add_shouldThrowVehicleAlreadyExists_whenExists() {

        // Given
        VehicleLocationRequest req = VehicleLocationRequest.builder()
                .vehicleName("car_1")
                .latitude(41.0)
                .longitude(29.0)
                .build();

        // When
        when(geoOperations.position(anyString(), eq("car_1")))
                .thenReturn(List.of(new Point(29.01, 41.02)));
        when(geoOperations.hash(anyString(), eq("car_1")))
                .thenReturn(List.of("hash1"));

        // Then
        VehicleAlreadyExistsException ex =
                assertThrows(VehicleAlreadyExistsException.class, () -> service.add(req));

        assertEquals("car_1", ex.getVehicleName());
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals(CustomError.Header.API_ERROR, ex.getHeader());
        assertEquals("vehicle.already.exists", ex.getMessage());

        // Verify
        verify(geoOperations).position(anyString(), eq("car_1"));
        verify(geoOperations).hash(anyString(), eq("car_1"));
        verify(geoOperations, never()).add(anyString(), any(Point.class), anyString());

    }

    @Test
    void add_shouldRethrowApiException_whenVehicleAlreadyExists() {

        // Given
        VehicleLocationRequest req = VehicleLocationRequest.builder()
                .vehicleName("car_1")
                .latitude(41.0)
                .longitude(29.0)
                .build();

        // When
        when(geoOperations.position(anyString(), eq("car_1")))
                .thenReturn(List.of(new Point(29.01, 41.02)));
        when(geoOperations.hash(anyString(), eq("car_1")))
                .thenReturn(List.of("hash1"));

        // Then
        VehicleAlreadyExistsException ex =
                assertThrows(VehicleAlreadyExistsException.class, () -> service.add(req));

        assertEquals("car_1", ex.getVehicleName());
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        assertEquals(CustomError.Header.API_ERROR, ex.getHeader());

        // Verify
        verify(geoOperations).position(anyString(), eq("car_1"));
        verify(geoOperations).hash(anyString(), eq("car_1"));
        verify(geoOperations, never()).add(anyString(), any(Point.class), anyString());

    }

    @Test
    void add_shouldWrapDataAccessException_intoGeoSearchFailedException() {

        // Given
        VehicleLocationRequest req = VehicleLocationRequest.builder()
                .vehicleName("car_new")
                .latitude(41.0)
                .longitude(29.0)
                .build();

        // When
        when(geoOperations.position(anyString(), eq("car_new")))
                .thenReturn(List.of());
        when(geoOperations.add(anyString(), any(Point.class), eq("car_new")))
                .thenThrow(new DataAccessResourceFailureException("redis write failed"));

        // Then
        GeoSearchFailedException ex =
                assertThrows(GeoSearchFailedException.class, () -> service.add(req));

        assertNotNull(ex.getMessage());
        assertEquals("geo.search.failed", ex.getMessage());

        // Verify
        verify(geoOperations).position(anyString(), eq("car_new"));
        verify(geoOperations).add(anyString(), any(Point.class), eq("car_new"));
        verify(geoOperations, never()).hash(anyString(), anyString());

    }

    @Test
    void add_shouldWrapGeneralException_intoGeoSearchFailedException() {

        // Given
        VehicleLocationRequest req = VehicleLocationRequest.builder()
                .vehicleName("car_new")
                .latitude(41.0)
                .longitude(29.0)
                .build();

        // When
        when(geoOperations.position(anyString(), eq("car_new")))
                .thenReturn(List.of());
        when(geoOperations.add(anyString(), any(Point.class), eq("car_new")))
                .thenReturn(1L);
        when(geoOperations.hash(anyString(), eq("car_new")))
                .thenThrow(new IllegalStateException("boom"));

        // Then
        GeoSearchFailedException ex =
                assertThrows(GeoSearchFailedException.class, () -> service.add(req));

        assertEquals("geo.search.failed", ex.getMessage());

        // verify
        verify(geoOperations).position(anyString(), eq("car_new"));
        verify(geoOperations).add(anyString(), any(Point.class), eq("car_new"));
        verify(geoOperations).hash(anyString(), eq("car_new"));

    }

    @Test
    void findByName_shouldRethrowApiException_whenGeoOperationsThrowsApiException() {
        // Given
        String name = "car_missing";
        VehicleNotFoundException apiEx = new VehicleNotFoundException(name);

        // When
        when(geoOperations.position(anyString(), eq(name))).thenThrow(apiEx);

        // Then
        VehicleNotFoundException thrown =
                assertThrows(VehicleNotFoundException.class, () -> service.findByName(name));

        assertSame(apiEx, thrown);

        // Verify
        verify(geoOperations).position(anyString(), eq(name));
        verify(geoOperations, never()).hash(anyString(), anyString());

    }

    @Test
    void findByName_shouldWrapDataAccessException_intoGeoSearchFailedException() {

        // Given
        String name = "car_redis_fail";

        // When
        when(geoOperations.position(anyString(), eq(name)))
                .thenThrow(new DataAccessResourceFailureException("redis down"));

        // Then
        GeoSearchFailedException ex =
                assertThrows(GeoSearchFailedException.class, () -> service.findByName(name));

        assertEquals("geo.search.failed", ex.getMessage());

        // Verify
        verify(geoOperations).position(anyString(), eq(name));
        verify(geoOperations, never()).hash(anyString(), anyString());

    }

    @Test
    void findByName_shouldWrapGeneralException_intoGeoSearchFailedException() {

        // Given
        String name = "car_boom";

        // When
        when(geoOperations.position(anyString(), eq(name)))
                .thenThrow(new IllegalStateException("boom"));

        // Then
        GeoSearchFailedException ex =
                assertThrows(GeoSearchFailedException.class, () -> service.findByName(name));

        assertEquals("geo.search.failed", ex.getMessage());

        // Verify
        verify(geoOperations).position(anyString(), eq(name));
        verify(geoOperations, never()).hash(anyString(), anyString());

    }

    @Test
    void findNearestVehicles_shouldReturnMappedCustomPage_whenSuccess() {

        // Given
        FindNearestByPointRequest req = spy(
                FindNearestByPointRequest.builder()
                        .longitude(29.012)
                        .latitude(41.022)
                        .km(10)
                        .pagination(CustomPaging.builder().pageNumber(1).pageSize(5).build())
                        .sorting(CustomSorting.builder().sortBy("distance").sortDirection("DESC").build())
                        .build()
        );

        Pageable pageable = PageRequest.of(1, 5, Sort.by(Sort.Direction.DESC, "distance"));
        doReturn(pageable).when(req).toPageable();

        GeoResults<RedisGeoCommands.GeoLocation<String>> pagedResults = geoResultsWithKmDistance(
                "car_1", new Point(29.01, 41.02), 1.1,
                "car_2", new Point(29.02, 41.03), 2.2
        );

        GeoResults<RedisGeoCommands.GeoLocation<String>> countResults = geoResultsWithKmDistance(
                "car_1", new Point(29.01, 41.02), 1.1,
                "car_2", new Point(29.02, 41.03), 2.2,
                "car_3", new Point(29.03, 41.04), 3.3
        );

        when(geoOperations.position(eq(KEY), anyString()))
                .thenAnswer(inv -> List.of(pointOf(inv.getArgument(1))));
        when(geoOperations.hash(eq(KEY), anyString()))
                .thenAnswer(inv -> List.of("hash_" + inv.getArgument(1)));

        when(geoOperations.position(eq(KEY), ArgumentMatchers.<String[]>any()))
                .thenAnswer(inv -> {
                    Object arg = inv.getArgument(1);
                    List<String> members = normalizeMembers(arg);
                    List<Point> out = new ArrayList<>();
                    for (String m : members) out.add(pointOf(m));
                    return out;
                });

        when(geoOperations.hash(eq(KEY), ArgumentMatchers.<String[]>any()))
                .thenAnswer(inv -> {
                    Object arg = inv.getArgument(1);
                    List<String> members = normalizeMembers(arg);
                    List<String> out = new ArrayList<>();
                    for (String m : members) out.add("hash_" + m);
                    return out;
                });
        when(geoOperations.radius(eq(KEY), any(Circle.class), any(RedisGeoCommands.GeoRadiusCommandArgs.class)))
                .thenReturn(pagedResults)
                .thenReturn(countResults);

        CustomPage<VehicleLocation> expected = geoResultsToCustomPageMapper.mapToCustomPage(
                pagedResults,
                pageable,
                3L,
                KEY,
                null,
                geoOperations
        );

        // When
        CustomPage<VehicleLocation> out = service.findNearestVehicles(req);

        // Then
        assertNotNull(out);
        assertEquals(expected.getTotalElementCount(), out.getTotalElementCount());
        assertEquals(expected.getPageNumber(), out.getPageNumber());
        assertEquals(expected.getPageSize(), out.getPageSize());
        assertEquals(expected.getTotalPageCount(), out.getTotalPageCount());
        assertEquals(expected.getContent(), out.getContent());

        // Verify radius called twice
        verify(geoOperations, times(2))
                .radius(eq(KEY), any(Circle.class), any(RedisGeoCommands.GeoRadiusCommandArgs.class));
    }


    @Test
    void findNearestVehiclesByMember_shouldThrowVehicleNotFound_whenCenterMissing() {

        // Given
        FindNearestByMemberRequest req = FindNearestByMemberRequest.builder()
                .centerVehicleName("center_missing")
                .km(10)
                .pagination(CustomPaging.builder().pageNumber(1).pageSize(5).build())
                .build();

        // When
        when(geoOperations.position(anyString(), eq("center_missing"))).thenReturn(List.of());

        // Then
        VehicleNotFoundException ex = assertThrows(VehicleNotFoundException.class, () -> service.findNearestVehiclesByMember(req));
        assertEquals("center_missing", ex.getVehicleName());
        assertEquals(CustomError.Header.API_ERROR, ex.getHeader());

        // Verify
        verify(geoOperations).position(anyString(), eq("center_missing"));
        verify(geoOperations, never()).radius(anyString(), anyString(), any(Distance.class), any(RedisGeoCommands.GeoRadiusCommandArgs.class));

    }

    @Test
    void findNearestVehiclesByMember_shouldExcludeCenterFromTotal_whenCenterIncludedInCount() {

        // Given
        FindNearestByMemberRequest req = spy(
                FindNearestByMemberRequest.builder()
                        .centerVehicleName("car_center")
                        .km(10)
                        .pagination(CustomPaging.builder().pageNumber(1).pageSize(5).build())
                        .build()
        );

        Pageable pageable = PageRequest.of(1, 5, Sort.by(Sort.Direction.ASC, "distance"));
        doReturn(pageable).when(req).toPageable();

        GeoResults<RedisGeoCommands.GeoLocation<String>> pagedResults = geoResultsWithKmDistance(
                "car_center", new Point(29.01, 41.02), 0.0,
                "car_1",      new Point(29.02, 41.03), 1.1
        );

        GeoResults<RedisGeoCommands.GeoLocation<String>> countResults = geoResultsWithKmDistance(
                "car_center", new Point(29.01, 41.02), 0.0,
                "car_1",      new Point(29.02, 41.03), 1.1,
                "car_2",      new Point(29.03, 41.04), 2.2
        );

        // When
        when(geoOperations.position(eq(KEY), eq("car_center")))
                .thenReturn(List.of(new Point(29.01, 41.02)));
        when(geoOperations.position(eq(KEY), eq("car_1")))
                .thenReturn(List.of(new Point(29.02, 41.03)));
        when(geoOperations.position(eq(KEY), eq("car_2")))
                .thenReturn(List.of(new Point(29.03, 41.04)));
        when(geoOperations.hash(eq(KEY), eq("car_center")))
                .thenReturn(List.of("hash_car_center"));
        when(geoOperations.hash(eq(KEY), eq("car_1")))
                .thenReturn(List.of("hash_car_1"));
        when(geoOperations.hash(eq(KEY), eq("car_2")))
                .thenReturn(List.of("hash_car_2"));
        when(geoOperations.position(eq(KEY), ArgumentMatchers.<String[]>any()))
                .thenAnswer(inv -> toPoints(inv.getArgument(1)));
        when(geoOperations.hash(eq(KEY), ArgumentMatchers.<String[]>any()))
                .thenAnswer(inv -> toHashes(inv.getArgument(1)));
        when(geoOperations.radius(eq(KEY), eq("car_center"), any(Distance.class), any(RedisGeoCommands.GeoRadiusCommandArgs.class)))
                .thenReturn(pagedResults)
                .thenReturn(countResults);
        when(geoOperations.radius(eq(KEY), any(Circle.class), any(RedisGeoCommands.GeoRadiusCommandArgs.class)))
                .thenReturn(pagedResults)
                .thenReturn(countResults);

        long expectedTotal = 2L;
        CustomPage<VehicleLocation> expected = geoResultsToCustomPageMapper.mapToCustomPage(
                pagedResults,
                pageable,
                expectedTotal,
                KEY,
                "car_center",
                geoOperations
        );

        // Then
        CustomPage<VehicleLocation> out = service.findNearestVehiclesByMember(req);

        assertEquals(expected.getTotalElementCount(), out.getTotalElementCount());
        assertEquals(expected.getPageNumber(), out.getPageNumber());
        assertEquals(expected.getPageSize(), out.getPageSize());
        assertEquals(expected.getTotalPageCount(), out.getTotalPageCount());
        assertEquals(expected.getContent(), out.getContent());

    }

    @Test
    void distanceBetweenVehiclesKm_shouldReturnVehicleDistance_whenSuccess() {

        // Given
        String a = "car_1";
        String b = "car_2";

        // When
        when(geoOperations.position(anyString(), eq(a))).thenReturn(List.of(new Point(29.01, 41.02)));
        when(geoOperations.hash(anyString(), eq(a))).thenReturn(List.of("hashA"));
        when(geoOperations.position(anyString(), eq(b))).thenReturn(List.of(new Point(29.02, 41.03)));
        when(geoOperations.hash(anyString(), eq(b))).thenReturn(List.of("hashB"));
        when(geoOperations.distance(eq("vehicle_location"), eq(a), eq(b), eq(Metrics.KILOMETERS)))
                .thenReturn(new Distance(3.5, Metrics.KILOMETERS));

        // Then
        VehicleDistance out = service.distanceBetweenVehiclesKm(a, b);
        assertNotNull(out);
        assertEquals(a, out.getVehicleA());
        assertEquals(b, out.getVehicleB());
        assertEquals(3.5, out.getDistanceKm());

        // Verify
        verify(geoOperations).distance(eq("vehicle_location"), eq(a), eq(b), eq(Metrics.KILOMETERS));

    }

    @Test
    void distanceBetweenVehiclesKm_shouldThrowVehicleNotFound_whenVehicleA_missing() {

        // Given
        String a = "missingA";
        String b = "car_2";

        // When
        when(geoOperations.position(anyString(), eq(a))).thenReturn(List.of()); // empty => not found

        // Then
        VehicleNotFoundException ex = assertThrows(VehicleNotFoundException.class,
                () -> service.distanceBetweenVehiclesKm(a, b));

        assertEquals(a, ex.getVehicleName());

        // Verify
        verify(geoOperations, never()).distance(anyString(), anyString(), anyString(), any());

    }

    @Test
    void distanceBetweenVehiclesKm_shouldThrowVehicleNotFound_whenVehicleB_missing() {

        // Given
        String a = "car_1";
        String b = "missingB";

        // When
        when(geoOperations.position(anyString(), eq(a))).thenReturn(List.of(new Point(29.01, 41.02)));
        when(geoOperations.hash(anyString(), eq(a))).thenReturn(List.of("hashA"));
        when(geoOperations.position(anyString(), eq(b))).thenReturn(List.of());

        // Then
        VehicleNotFoundException ex = assertThrows(VehicleNotFoundException.class,
                () -> service.distanceBetweenVehiclesKm(a, b));

        assertEquals(b, ex.getVehicleName());

        // Verify
        verify(geoOperations, never()).distance(anyString(), anyString(), anyString(), any());

    }

    @Test
    void distanceBetweenVehiclesKm_shouldThrowGeoSearchFailed_whenDistanceNull() {

        // Given
        String a = "car_1";
        String b = "car_2";

        // When
        when(geoOperations.position(anyString(), eq(a))).thenReturn(List.of(new Point(29.01, 41.02)));
        when(geoOperations.hash(anyString(), eq(a))).thenReturn(List.of("hashA"));
        when(geoOperations.position(anyString(), eq(b))).thenReturn(List.of(new Point(29.02, 41.03)));
        when(geoOperations.hash(anyString(), eq(b))).thenReturn(List.of("hashB"));
        when(geoOperations.distance(eq("vehicle_location"), eq(a), eq(b), eq(Metrics.KILOMETERS)))
                .thenReturn(null);

        // Then
        GeoSearchFailedException ex = assertThrows(GeoSearchFailedException.class,
                () -> service.distanceBetweenVehiclesKm(a, b));

        assertEquals("geo.search.failed", ex.getMessage());

    }

    @Test
    void findNearestVehiclesByMember_shouldReturnTotalZero_whenCountResultsNull() {

        // Given
        FindNearestByMemberRequest req = spy(
                FindNearestByMemberRequest.builder()
                        .centerVehicleName("car_center")
                        .km(10)
                        .pagination(CustomPaging.builder().pageNumber(1).pageSize(5).build())
                        .build()
        );

        Pageable pageable = PageRequest.of(1, 5);
        doReturn(pageable).when(req).toPageable();

        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> pagedList = new ArrayList<>();
        pagedList.add(new GeoResult<>(
                new RedisGeoCommands.GeoLocation<>("car_center", new Point(29.01, 41.02)),
                new Distance(0.0, Metrics.KILOMETERS)
        ));
        pagedList.add(new GeoResult<>(
                new RedisGeoCommands.GeoLocation<>("car_1", new Point(29.02, 41.03)),
                new Distance(1.1, Metrics.KILOMETERS)
        ));
        GeoResults<RedisGeoCommands.GeoLocation<String>> pagedResults =
                new GeoResults<>(pagedList);

        // When
        when(geoOperations.position(eq(KEY), anyString()))
                .thenAnswer(inv -> {
                    String member = inv.getArgument(1);
                    if ("car_center".equals(member)) return List.of(new Point(29.01, 41.02));
                    if ("car_1".equals(member)) return List.of(new Point(29.02, 41.03));
                    return List.of();
                });
        when(geoOperations.hash(eq(KEY), anyString()))
                .thenAnswer(inv -> {
                    String member = inv.getArgument(1);
                    if ("car_center".equals(member) || "car_1".equals(member)) return List.of("hash_" + member);
                    return List.of();
                });
        when(geoOperations.position(eq(KEY), ArgumentMatchers.<String[]>any()))
                .thenAnswer(inv -> {
                    Object[] args = inv.getArguments();

                    List<String> members = new ArrayList<>();
                    if (args.length == 2 && args[1] instanceof String[] arr) {
                        members.addAll(Arrays.asList(arr));
                    } else {
                        for (int i = 1; i < args.length; i++) {
                            if (args[i] instanceof String s) members.add(s);
                        }
                    }

                    List<Point> points = new ArrayList<>();
                    for (String m : members) {
                        if ("car_center".equals(m)) points.add(new Point(29.01, 41.02));
                        else if ("car_1".equals(m)) points.add(new Point(29.02, 41.03));
                    }
                    return points;
                });
        when(geoOperations.hash(eq(KEY), ArgumentMatchers.<String[]>any()))
                .thenAnswer(inv -> {
                    Object[] args = inv.getArguments();

                    List<String> members = new ArrayList<>();
                    if (args.length == 2 && args[1] instanceof String[] arr) {
                        members.addAll(Arrays.asList(arr));
                    } else {
                        for (int i = 1; i < args.length; i++) {
                            if (args[i] instanceof String s) members.add(s);
                        }
                    }

                    List<String> hashes = new ArrayList<>();
                    for (String m : members) {
                        if ("car_center".equals(m) || "car_1".equals(m)) hashes.add("hash_" + m);
                    }
                    return hashes;
                });

        when(geoOperations.radius(eq(KEY), eq("car_center"), any(Distance.class), any(RedisGeoCommands.GeoRadiusCommandArgs.class)))
                .thenReturn(pagedResults)
                .thenReturn(null);

        // When
        CustomPage<VehicleLocation> out = service.findNearestVehiclesByMember(req);

        // Then
        assertNotNull(out);
        assertEquals(0L, out.getTotalElementCount());

    }

    @Test
    void findNearestVehiclesByMember_shouldReturnTotalZero_whenCountContentNull() {

        // Given
        FindNearestByMemberRequest req = spy(
                FindNearestByMemberRequest.builder()
                        .centerVehicleName("car_center")
                        .km(10)
                        .pagination(CustomPaging.builder().pageNumber(1).pageSize(5).build())
                        .build()
        );

        Pageable pageable = PageRequest.of(1, 5);
        doReturn(pageable).when(req).toPageable();

        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> pagedList = new ArrayList<>();
        pagedList.add(new GeoResult<>(
                new RedisGeoCommands.GeoLocation<>("car_center", new Point(29.01, 41.02)),
                new Distance(0.0, Metrics.KILOMETERS)
        ));
        pagedList.add(new GeoResult<>(
                new RedisGeoCommands.GeoLocation<>("car_1", new Point(29.02, 41.03)),
                new Distance(1.1, Metrics.KILOMETERS)
        ));
        GeoResults<RedisGeoCommands.GeoLocation<String>> pagedResults =
                new GeoResults<>(pagedList);

        // When
        when(geoOperations.position(eq(KEY), anyString()))
                .thenAnswer(inv -> {
                    String m = inv.getArgument(1);
                    if ("car_center".equals(m)) return List.of(new Point(29.01, 41.02));
                    if ("car_1".equals(m)) return List.of(new Point(29.02, 41.03));
                    return List.of();
                });
        when(geoOperations.hash(eq(KEY), anyString()))
                .thenAnswer(inv -> {
                    String m = inv.getArgument(1);
                    if ("car_center".equals(m) || "car_1".equals(m)) return List.of("hash_" + m);
                    return List.of();
                });
        when(geoOperations.position(eq(KEY), ArgumentMatchers.<String[]>any()))
                .thenAnswer(inv -> {
                    Object[] args = inv.getArguments();
                    List<String> members = new ArrayList<>();

                    if (args.length == 2 && args[1] instanceof String[] arr) {
                        members.addAll(Arrays.asList(arr));
                    } else {
                        for (int i = 1; i < args.length; i++) {
                            if (args[i] instanceof String s) members.add(s);
                        }
                    }

                    List<Point> out = new ArrayList<>();
                    for (String m : members) {
                        if ("car_center".equals(m)) out.add(new Point(29.01, 41.02));
                        else if ("car_1".equals(m)) out.add(new Point(29.02, 41.03));
                    }
                    return out;
                });

        when(geoOperations.hash(eq(KEY), ArgumentMatchers.<String[]>any()))
                .thenAnswer(inv -> {
                    Object[] args = inv.getArguments();
                    List<String> members = new ArrayList<>();

                    if (args.length == 2 && args[1] instanceof String[] arr) {
                        members.addAll(Arrays.asList(arr));
                    } else {
                        for (int i = 1; i < args.length; i++) {
                            if (args[i] instanceof String s) members.add(s);
                        }
                    }

                    List<String> out = new ArrayList<>();
                    for (String m : members) {
                        if ("car_center".equals(m) || "car_1".equals(m)) out.add("hash_" + m);
                    }
                    return out;
                });


        GeoResults<RedisGeoCommands.GeoLocation<String>> countResults =
                mock(GeoResults.class);
        when(countResults.getContent()).thenReturn(null);

        when(geoOperations.radius(eq(KEY), eq("car_center"), any(Distance.class), any(RedisGeoCommands.GeoRadiusCommandArgs.class)))
                .thenReturn(pagedResults)
                .thenReturn(countResults);

        // Then
        CustomPage<VehicleLocation> out = service.findNearestVehiclesByMember(req);

        assertNotNull(out);
        assertEquals(0L, out.getTotalElementCount());

    }

    private GeoResults<RedisGeoCommands.GeoLocation<String>> geoResultsOf(
            String name1, Point p1, String name2, Point p2) {
        return new GeoResults<>(List.of(
                new GeoResult<>(new RedisGeoCommands.GeoLocation<>(name1, p1), ZERO_KM),
                new GeoResult<>(new RedisGeoCommands.GeoLocation<>(name2, p2), ZERO_KM)
        ));
    }

    private GeoResults<RedisGeoCommands.GeoLocation<String>> geoResultsOf(
            String name1, Point p1, String name2, Point p2, String name3, Point p3) {
        return new GeoResults<>(List.of(
                new GeoResult<>(new RedisGeoCommands.GeoLocation<>(name1, p1), ZERO_KM),
                new GeoResult<>(new RedisGeoCommands.GeoLocation<>(name2, p2), ZERO_KM),
                new GeoResult<>(new RedisGeoCommands.GeoLocation<>(name3, p3), ZERO_KM)
        ));
    }

    private GeoResults<RedisGeoCommands.GeoLocation<String>> geoResultsWithKmDistance(Object... triples) {
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> list = new ArrayList<>();

        for (int i = 0; i < triples.length; i += 3) {
            String name = (String) triples[i];
            Point point = (Point) triples[i + 1];
            double km = (double) triples[i + 2];

            RedisGeoCommands.GeoLocation<String> loc = new RedisGeoCommands.GeoLocation<>(name, point);
            Distance dist = new Distance(km, Metrics.KILOMETERS);

            list.add(new GeoResult<>(loc, dist));
        }
        return new GeoResults<>(list);
    }

    private List<Point> toPoints(Object membersArg) {
        List<String> members = normalizeMembers(membersArg);
        List<Point> points = new ArrayList<>();
        for (String m : members) {
            switch (m) {
                case "car_center" -> points.add(new Point(29.01, 41.02));
                case "car_1"      -> points.add(new Point(29.02, 41.03));
                case "car_2"      -> points.add(new Point(29.03, 41.04));
                default           -> points.add(new Point(0, 0));
            }
        }
        return points;
    }

    private List<String> toHashes(Object membersArg) {
        List<String> members = normalizeMembers(membersArg);
        List<String> hashes = new ArrayList<>();
        for (String m : members) {
            hashes.add("hash_" + m);
        }
        return hashes;
    }

    private List<String> normalizeMembers(Object membersArg) {
        if (membersArg instanceof String[] arr) {
            return Arrays.asList(arr);
        }
        if (membersArg instanceof String s) {
            return List.of(s);
        }
        throw new IllegalArgumentException("Unexpected members arg type: " + membersArg);
    }

    private Point pointOf(String vehicle) {
        return switch (vehicle) {
            case "car_1" -> new Point(29.01, 41.02);
            case "car_2" -> new Point(29.02, 41.03);
            case "car_3" -> new Point(29.03, 41.04);
            default -> new Point(0, 0);
        };
    }

}