package com.mwu.geodistance.location.model.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDistanceRequest {

    @NotBlank(message = "vehicleA must not be blank")
    private String vehicleA;

    @NotBlank(message = "vehicleB must not be blank")
    private String vehicleB;

    //表示该方法必须返回 true 才能通过验证。如果返回 false，则会抛出验证异常，并显示指定的错误消息："vehicleA and vehicleB must be different"。
    @AssertTrue(message = "vehicleA and vehicleB must be different")
    public boolean isDifferent() {
        if (vehicleA == null || vehicleB == null) return true;
        return !vehicleA.trim().equalsIgnoreCase(vehicleB.trim());
    }

}

