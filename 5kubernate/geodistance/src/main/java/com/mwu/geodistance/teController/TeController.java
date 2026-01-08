package com.mwu.geodistance.teController;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.stream.Location;

@RestController
public class TeController {
    @PostMapping("/locations")
    public ResponseEntity<Location> createLocation(
            @Valid @RequestBody LocationRequest request) {
        // 如果 @Valid 验证失败，抛出 MethodArgumentNotValidException
        return ResponseEntity.ok().build();
    }
}
