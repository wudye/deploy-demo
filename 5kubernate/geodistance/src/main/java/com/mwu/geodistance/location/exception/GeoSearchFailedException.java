package com.mwu.geodistance.location.exception;


import com.mwu.geodistance.common.exception.ApiException;
import com.mwu.geodistance.common.exception.error.CustomError;
import org.springframework.http.HttpStatus;

public class GeoSearchFailedException extends ApiException {

    public GeoSearchFailedException(String details) {
        super("geo.search.failed", details);
    }

    @Override
    public HttpStatus getStatus() { return HttpStatus.INTERNAL_SERVER_ERROR; }

    @Override
    public CustomError.Header getHeader() { return CustomError.Header.API_ERROR; }

}


