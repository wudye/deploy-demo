package com.mwu.geodistance.teController;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class LocationRequest {
    @NotBlank(message = "名称不能为空")
    private String name;

    @Min(value = -180, message = "经度不能小于 -180")
    @Max(value = 180, message = "经度不能大于 180")
    private Double longitude;

    @Min(value = -90, message = "纬度不能小于 -90")
    @Max(value = 90, message = "纬度不能大于 90")
    private Double latitude;
}
