package com.mwu.geodistance.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private String name;          // 地点名称
    private String address;        // 地址
    private Double longitude;      // 经度
    private Double latitude;       // 纬度
}

