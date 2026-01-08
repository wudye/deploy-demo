package com.mwu.geodistance.location.model.dto.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class VehicleDistanceRequestTest {

    @Test
    void isDifferent_whenBothNull_returnsTrue() {
        VehicleDistanceRequest req = VehicleDistanceRequest.builder()
                .vehicleA(null)
                .vehicleB(null)
                .build();

        assertThat(req.isDifferent()).isTrue();
    }

    @Test
    void isDifferent_whenVehicleAIsNull_returnsTrue() {
        VehicleDistanceRequest req = VehicleDistanceRequest.builder()
                .vehicleA(null)
                .vehicleB("CAR_B")
                .build();

        assertThat(req.isDifferent()).isTrue();
    }

    @Test
    void isDifferent_whenVehicleBIsNull_returnsTrue() {
        VehicleDistanceRequest req = VehicleDistanceRequest.builder()
                .vehicleA("CAR_A")
                .vehicleB(null)
                .build();

        assertThat(req.isDifferent()).isTrue();
    }

    @Test
    void isDifferent_whenSameIgnoringCaseAndTrim_returnsFalse() {
        VehicleDistanceRequest req = VehicleDistanceRequest.builder()
                .vehicleA("  car_1 ")
                .vehicleB("CAR_1")
                .build();

        assertThat(req.isDifferent()).isFalse();
    }

    @Test
    void isDifferent_whenDifferent_returnsTrue() {
        VehicleDistanceRequest req = VehicleDistanceRequest.builder()
                .vehicleA("CAR_1")
                .vehicleB("CAR_2")
                .build();

        assertThat(req.isDifferent()).isTrue();
    }

    @Test
    void isDifferent_whenSameButDifferentWhitespace_returnsFalse() {
        VehicleDistanceRequest req = VehicleDistanceRequest.builder()
                .vehicleA("CAR_1")
                .vehicleB("   CAR_1   ")
                .build();

        assertThat(req.isDifferent()).isFalse();
    }

    @Test
    void isDifferent_whenOneIsEmptyOtherNot_returnsTrue() {
        VehicleDistanceRequest req = VehicleDistanceRequest.builder()
                .vehicleA("")
                .vehicleB("CAR_1")
                .build();

        assertThat(req.isDifferent()).isTrue();
    }

}