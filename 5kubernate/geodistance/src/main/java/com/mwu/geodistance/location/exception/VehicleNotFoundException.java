package com.mwu.geodistance.location.exception;

import com.mwu.geodistance.common.exception.ApiException;
import com.mwu.geodistance.common.exception.error.CustomError;
import org.springframework.http.HttpStatus;

public class VehicleNotFoundException extends ApiException {

    private final String vehicleName;

    public VehicleNotFoundException(String vehicleName) {
        super("vehicle.not.found", vehicleName);
        this.vehicleName = vehicleName;
    }

    @Override
    public HttpStatus getStatus() { return HttpStatus.NOT_FOUND; }

    @Override
    public CustomError.Header getHeader() { return CustomError.Header.API_ERROR; }

    public String getVehicleName() { return vehicleName; }

}
