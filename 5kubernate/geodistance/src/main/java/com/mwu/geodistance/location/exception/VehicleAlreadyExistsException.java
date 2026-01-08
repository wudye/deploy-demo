package com.mwu.geodistance.location.exception;

import com.mwu.geodistance.common.exception.ApiException;
import com.mwu.geodistance.common.exception.error.CustomError;
import org.springframework.http.HttpStatus;

public class VehicleAlreadyExistsException extends ApiException {

    private final String vehicleName;

    public VehicleAlreadyExistsException(String vehicleName) {
        super("vehicle.already.exists", vehicleName);
        this.vehicleName = vehicleName;
    }

    @Override
    public HttpStatus getStatus() { return HttpStatus.CONFLICT; }

    @Override
    public CustomError.Header getHeader() { return CustomError.Header.API_ERROR; }

    public String getVehicleName() { return vehicleName; }

}