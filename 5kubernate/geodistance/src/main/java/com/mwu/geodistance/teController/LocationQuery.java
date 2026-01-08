package com.mwu.geodistance.teController;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class LocationQuery {
    @Min(1)
    private Integer limit;

    private String keyword;
}