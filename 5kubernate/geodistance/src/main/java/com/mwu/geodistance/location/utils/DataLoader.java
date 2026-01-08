package com.mwu.geodistance.location.utils;


import com.mwu.geodistance.location.model.dto.request.VehicleLocationRequest;
import com.mwu.geodistance.location.service.GeoLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final GeoLocationService geoLocationService;
    private final StringRedisTemplate stringRedisTemplate;

    @Bean
    public CommandLineRunner loadVehicleLocations() {
        return args -> {

            stringRedisTemplate.delete("vehicle_location");

            geoLocationService.add(VehicleLocationRequest.builder()
                    .vehicleName("car_1")
                    .latitude(41.0210)
                    .longitude(29.0100)
                    .build());

            geoLocationService.add(VehicleLocationRequest.builder()
                    .vehicleName("car_2")
                    .latitude(41.0250)
                    .longitude(29.0150)
                    .build());

            geoLocationService.add(VehicleLocationRequest.builder()
                    .vehicleName("car_3")
                    .latitude(41.0300)
                    .longitude(29.0200)
                    .build());

            geoLocationService.add(VehicleLocationRequest.builder()
                    .vehicleName("car_4")
                    .latitude(41.0500)
                    .longitude(29.0600)
                    .build());

            geoLocationService.add(VehicleLocationRequest.builder()
                    .vehicleName("car_5")
                    .latitude(41.0800)
                    .longitude(29.1000)
                    .build());
        };
    }

}
