package com.mwu.geodistance.teController;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.stream.Location;
import java.util.List;

@RestController
@Validated  // ← 关键：启用方法级别的验证
public class LocationController {

    @GetMapping("/locations/{id}")
    public ResponseEntity<Location> getLocation(
            @PathVariable @Min(value = 1, message = "ID 必须大于 0") Long id) {
        // 如果 id = 0，抛出 ConstraintViolationException
        return ResponseEntity.ok().build();
    }

    @GetMapping("/locations/range")
    public ResponseEntity<List<Location>> getLocationsInRange(
            @RequestParam @Min(1) @Max(100) Integer limit) {
        // 如果 limit = 200，抛出 ConstraintViolationException
        return ResponseEntity.ok().build();
    }

    @GetMapping("/locations")
    public ResponseEntity<Location> getLocation(
            @Validated LocationQuery query) {
        // 如果查询参数绑定失败，抛出 BindException
        return ResponseEntity.ok().build();
    }
}
