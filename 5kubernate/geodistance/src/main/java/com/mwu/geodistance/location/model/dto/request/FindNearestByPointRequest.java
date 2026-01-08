package com.mwu.geodistance.location.model.dto.request;

import com.mwu.geodistance.common.model.dto.request.CustomPagingRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class FindNearestByPointRequest extends CustomPagingRequest {

    @NotNull
    private Double longitude;

    @NotNull
    private Double latitude;

    @NotNull
    @Min(1)
    @Max(500)
    private Integer km;
}

