package com.mwu.geodistance.location.model.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VehicleLocationRequest {

    @NotBlank(message = "vehicleName must not be blank")
    private String vehicleName;

    @NotNull(message = "latitude must not be null")
    @Min(value = -90, message = "latitude must be >= -90")
    @Max(value = 90, message = "latitude must be <= 90")
    private Double latitude;

    @NotNull(message = "longitude must not be null")
    @Min(value = -180, message = "longitude must be >= -180")
    @Max(value = 180, message = "longitude must be <= 180")
    private Double longitude;

}

