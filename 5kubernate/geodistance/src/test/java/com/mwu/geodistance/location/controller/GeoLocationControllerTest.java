package com.mwu.geodistance.location.controller;


import com.mwu.geodistance.base.AbstractRestControllerTest;
import com.mwu.geodistance.common.model.CustomPage;
import com.mwu.geodistance.location.model.VehicleDistance;
import com.mwu.geodistance.location.model.VehicleLocation;
import com.mwu.geodistance.location.model.dto.request.FindNearestByMemberRequest;
import com.mwu.geodistance.location.model.dto.request.FindNearestByPointRequest;
import com.mwu.geodistance.location.model.dto.request.VehicleLocationRequest;
import com.mwu.geodistance.location.model.dto.response.VehicleDistanceResponse;
import com.mwu.geodistance.location.model.dto.response.VehicleLocationResponse;
import com.mwu.geodistance.location.model.mapper.CustomPageVehicleLocationToResponseMapper;
import com.mwu.geodistance.location.model.mapper.VehicleDistanceToResponseMapper;
import com.mwu.geodistance.location.model.mapper.VehicleLocationToVehicleLocationResponseMapper;
import com.mwu.geodistance.location.service.GeoLocationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.data.geo.Point;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@EnabledIf("isDockerAvailable")
class GeoLocationControllerTest extends AbstractRestControllerTest {

    /**
     * 检查 Docker 是否可用
     * 如果 Docker 不可用，测试将被跳过
     *
     * 支持以下方式配置远程 Docker：
     * 1. 环境变量: DOCKER_HOST=tcp://192.168.80.131:2375
     * 2. Java 系统属性: -Ddocker.host=tcp://192.168.80.131:2375
     */
    static boolean isDockerAvailable() {
        try {
            org.testcontainers.DockerClientFactory.instance().client();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @MockitoBean
    private GeoLocationService geoLocationService;

    private static final VehicleLocationToVehicleLocationResponseMapper locationToVehicleLocationResponseMapper =
            VehicleLocationToVehicleLocationResponseMapper.initialize();

    private static final CustomPageVehicleLocationToResponseMapper customPageVehicleLocationToResponseMapper =
            CustomPageVehicleLocationToResponseMapper.initialize();

    private static final VehicleDistanceToResponseMapper vehicleDistanceToResponseMapper =  VehicleDistanceToResponseMapper.initialize();

    @Test
    void add_shouldReturn200_andMappedResponse() throws Exception {

        // Given
        VehicleLocation domain = VehicleLocation.builder()
                .vehicleName("car_1")
                .point(new Point(29.01, 41.02))
                .build();

        VehicleLocationResponse expected = locationToVehicleLocationResponseMapper.mapToResponse(domain);

        // When
        when(geoLocationService.add(any(VehicleLocationRequest.class)))
                .thenReturn(domain);

        String body = objectMapper.writeValueAsString(Map.of(
                "vehicleName", "car_1",
                "longitude", 29.01,
                "latitude", 41.02
        ));

        // Then
        mockMvc.perform(post("/api/vehicles/location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value("200 OK"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.time").isNotEmpty())
                .andExpect(jsonPath("$.response").exists())
                .andExpect(jsonPath("$.response.vehicleName").value(expected.getVehicleName()))
                .andExpect(jsonPath("$.response.point.x").value(expected.getPoint().getX()))
                .andExpect(jsonPath("$.response.point.y").value(expected.getPoint().getY()));

        // Verify
        verify(geoLocationService,atLeastOnce()).add(any(VehicleLocationRequest.class));
        verifyNoMoreInteractions(geoLocationService);

    }

    @Test
    void nearestByPoint_shouldReturn200_andMappedCustomPage() throws Exception {

        // Given
        CustomPage<VehicleLocation> domainPage = CustomPage.<VehicleLocation>builder()
                .content(List.of(
                        VehicleLocation.builder().vehicleName("car_1").point(new Point(29.01, 41.02)).build(),
                        VehicleLocation.builder().vehicleName("car_2").point(new Point(29.02, 41.03)).build()
                ))
                .pageNumber(1)
                .pageSize(5)
                .totalElementCount(2L)
                .totalPageCount(1)
                .build();

        CustomPage<VehicleLocationResponse> expected =
                customPageVehicleLocationToResponseMapper.mapToResponsePage(domainPage);

        // When
        when(geoLocationService.findNearestVehicles(any(FindNearestByPointRequest.class)))
                .thenReturn(domainPage);

        String body = objectMapper.writeValueAsString(Map.of(
                "longitude", 29.012,
                "latitude", 41.022,
                "km", 10,
                "pagination", Map.of("pageNumber", 1, "pageSize", 5),
                "sorting", Map.of("sortBy", "distance", "sortDirection", "DESC")
        ));

        // Then
        mockMvc.perform(post("/api/vehicles/nearest/point")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value("200 OK"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.time").isNotEmpty())
                .andExpect(jsonPath("$.response").exists())
                .andExpect(jsonPath("$.response.pageNumber").value(expected.getPageNumber()))
                .andExpect(jsonPath("$.response.pageSize").value(expected.getPageSize()))
                .andExpect(jsonPath("$.response.totalElementCount").value(expected.getTotalElementCount()))
                .andExpect(jsonPath("$.response.totalPageCount").value(expected.getTotalPageCount()))
                .andExpect(jsonPath("$.response.content", hasSize(expected.getContent().size())))
                .andExpect(jsonPath("$.response.content[*].vehicleName",
                        containsInAnyOrder(
                                expected.getContent().stream().map(VehicleLocationResponse::getVehicleName).toArray()
                        )))
                .andExpect(jsonPath("$.response.content[*].point.x",
                        containsInAnyOrder(
                                expected.getContent().stream().map(r -> r.getPoint().getX()).toArray()
                        )))
                .andExpect(jsonPath("$.response.content[*].point.y",
                        containsInAnyOrder(
                                expected.getContent().stream().map(r -> r.getPoint().getY()).toArray()
                        )));

        // Verify
        verify(geoLocationService, times(1)).findNearestVehicles(any(FindNearestByPointRequest.class));

    }

    @Test
    void nearestByMember_shouldReturn200_andMappedCustomPage() throws Exception {

        // Given
        CustomPage<VehicleLocation> domainPage = CustomPage.<VehicleLocation>builder()
                .content(List.of(
                        VehicleLocation.builder().vehicleName("car_1").point(new Point(29.02, 41.03)).build()
                ))
                .pageNumber(1)
                .pageSize(5)
                .totalElementCount(1L)
                .totalPageCount(1)
                .build();

        CustomPage<VehicleLocationResponse> expected =
                customPageVehicleLocationToResponseMapper.mapToResponsePage(domainPage);

        // When
        when(geoLocationService.findNearestVehiclesByMember(any(FindNearestByMemberRequest.class)))
                .thenReturn(domainPage);

        String body = objectMapper.writeValueAsString(Map.of(
                "centerVehicleName", "car_center",
                "km", 10,
                "pagination", Map.of("pageNumber", 1, "pageSize", 5),
                "sorting", Map.of("sortBy", "distance", "sortDirection", "ASC")
        ));

        // Then
        mockMvc.perform(post("/api/vehicles/nearest/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.httpStatus").value("200 OK"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.time").isNotEmpty())
                .andExpect(jsonPath("$.response").exists())
                .andExpect(jsonPath("$.response.pageNumber").value(expected.getPageNumber()))
                .andExpect(jsonPath("$.response.pageSize").value(expected.getPageSize()))
                .andExpect(jsonPath("$.response.totalElementCount").value(expected.getTotalElementCount()))
                .andExpect(jsonPath("$.response.totalPageCount").value(expected.getTotalPageCount()))
                .andExpect(jsonPath("$.response.content", hasSize(expected.getContent().size())))
                .andExpect(jsonPath("$.response.content[0].vehicleName").value(expected.getContent().getFirst().getVehicleName()))
                .andExpect(jsonPath("$.response.content[0].point.x").value(expected.getContent().getFirst().getPoint().getX()))
                .andExpect(jsonPath("$.response.content[0].point.y").value(expected.getContent().getFirst().getPoint().getY()));

        // Verify
        verify(geoLocationService, atLeastOnce())
                .findNearestVehiclesByMember(any(FindNearestByMemberRequest.class));

    }

    @Test
    void distanceBetweenVehicles_shouldReturn200_andMappedDistance() throws Exception {

        // Given
        VehicleDistance domain = VehicleDistance.builder()
                .vehicleA("car_1")
                .vehicleB("car_2")
                .distanceKm(3.4)
                .build();

        VehicleDistanceResponse expected = vehicleDistanceToResponseMapper.mapToResponse(domain);

        // When
        when(geoLocationService.distanceBetweenVehiclesKm(eq("car_1"), eq("car_2")))
                .thenReturn(domain);

        String body = objectMapper.writeValueAsString(Map.of(
                "vehicleA", "car_1",
                "vehicleB", "car_2"
        ));

        // Then
        mockMvc.perform(post("/api/vehicles/distance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.httpStatus").value("200 OK"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.time").isNotEmpty())
                .andExpect(jsonPath("$.response").exists())
                .andExpect(jsonPath("$.response.vehicleA").value(expected.getVehicleA()))
                .andExpect(jsonPath("$.response.vehicleB").value(expected.getVehicleB()))
                .andExpect(jsonPath("$.response.distanceKm").value(closeTo(expected.getDistanceKm(), 0.0001)));

        // Verify
        verify(geoLocationService, times(1)).distanceBetweenVehiclesKm("car_1", "car_2");

    }

    @Test
    void nearestByMember_shouldReturn400_whenRequestInvalid_andNotCallService() throws Exception {

        // Given
        String body = objectMapper.writeValueAsString(Map.of(
                "km", 10,
                "pagination", Map.of("pageNumber", 1, "pageSize", 5)
        ));

        // When & Then
        mockMvc.perform(post("/api/vehicles/nearest/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

}