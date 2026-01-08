package com.mwu.geodistance.service;

import com.mwu.geodistance.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GeoServiceTest {

    @Autowired
    private GeoService geoService;

    private static final String GEO_KEY = "locations";

    @BeforeEach
    void setUp() {
        // 清理测试数据
        geoService.removeLocation(GEO_KEY, 
            "天安门", "外滩", "西湖", "故宫", "颐和园");
    }

    @Test
    void testAddLocation() {
        Location location = Location.builder()
            .name("天安门")
            .address("北京市东城区")
            .longitude(116.397128)
            .latitude(39.916527)
            .build();

        geoService.addLocation(GEO_KEY, location);

        Point point = geoService.getLocation(GEO_KEY, "天安门");
        assertThat(point).isNotNull();
        assertThat(point.getX()).isEqualTo(116.397128);
        assertThat(point.getY()).isEqualTo(39.916527);
    }

    @Test
    void testCalculateDistance() {
        // 北京天安门
        geoService.addLocation(GEO_KEY, Location.builder()
            .name("天安门")
            .longitude(116.397128)
            .latitude(39.916527)
            .build());

        // 上海外滩
        geoService.addLocation(GEO_KEY, Location.builder()
            .name("外滩")
            .longitude(121.490317)
            .latitude(31.243898)
            .build());

        Double distance = geoService.calculateDistance(GEO_KEY, "天安门", "外滩");
        
        // 北京到上海约 1068 公里
        assertThat(distance).isGreaterThan(1000000);  // 米
        assertThat(distance).isLessThan(1100000);
    }

    @Test
    void testCalculateDistanceWithUnit() {
        geoService.addLocation(GEO_KEY, Location.builder()
            .name("天安门")
            .longitude(116.397128)
            .latitude(39.916527)
            .build());

        geoService.addLocation(GEO_KEY, Location.builder()
            .name("故宫")
            .longitude(116.397477)
            .latitude(39.918058)
            .build());

        Double distanceKm = geoService.calculateDistance(
            GEO_KEY, "天安门", "故宫", DistanceUnit.KILOMETERS);
        
        // 距离约 0.2 公里
        assertThat(distanceKm).isGreaterThan(0.1);
        assertThat(distanceKm).isLessThan(0.5);
    }

    @Test
    void testFindLocationsInRadius() {
        // 添加杭州周边的地点
        geoService.addLocation(GEO_KEY, Location.builder()
            .name("西湖")
            .longitude(120.153576)
            .latitude(30.242831)
            .build());

        geoService.addLocation(GEO_KEY, Location.builder()
            .name("灵隐寺")
            .longitude(120.131311)
            .latitude(30.243279)
            .build());

        geoService.addLocation(GEO_KEY, Location.builder()
            .name("千岛湖")
            .longitude(119.032988)
            .latitude(29.608716)
            .build());

        // 查找杭州周边 20 公里内的地点
        List<String> locations = geoService.findLocationsInRadius(
            GEO_KEY, 120.153576, 30.242831, 20.0);
        
        // 应该包含西湖和灵隐寺，不包含千岛湖
        assertThat(locations).contains("西湖", "灵隐寺");
        assertThat(locations).doesNotContain("千岛湖");
    }

    @Test
    void testGetGeoHash() {
        geoService.addLocation(GEO_KEY, Location.builder()
            .name("天安门")
            .longitude(116.397128)
            .latitude(39.916527)
            .build());

        String geoHash = geoService.getGeoHash(GEO_KEY, "天安门");
        
        assertThat(geoHash).isNotNull();
        assertThat(geoHash).hasSize(11);  // Redis 默认 11 位精度
    }
}
