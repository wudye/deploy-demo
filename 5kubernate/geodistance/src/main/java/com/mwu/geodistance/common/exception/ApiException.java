package com.mwu.geodistance.common.exception;


import com.mwu.geodistance.common.exception.error.CustomError;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base class for all custom API exceptions in the application.
 * Subclasses must define the associated {@link HttpStatus} and
 * {@link CustomError.Header} so that the global exception handler
 * can map them to a consistent {@link CustomError} response.
 */
@Getter
public abstract class ApiException extends RuntimeException {

    private final String messageKey;
    private final Object[] messageArgs;

    protected ApiException(String messageKey, Object... messageArgs) {
        super(messageKey);
        this.messageKey = messageKey;
        this.messageArgs = messageArgs;
    }

    public abstract HttpStatus getStatus();

    public abstract CustomError.Header getHeader();
}
