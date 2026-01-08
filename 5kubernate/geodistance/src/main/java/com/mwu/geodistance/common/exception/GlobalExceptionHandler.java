package com.mwu.geodistance.common.exception;

import com.mwu.geodistance.common.exception.error.CustomError;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    private Locale locale() {
        return LocaleContextHolder.getLocale();
    }


    private String msg(String key, Object... args) {
        return messageSource.getMessage(key, args, key, locale());
    }

    /**
     * Handles validation errors on request bodies annotated with {@code @Valid}.
     * Converts {@link MethodArgumentNotValidException} into a {@link CustomError}
     * with {@link CustomError.Header#VALIDATION_ERROR} and detailed
     * {@link CustomError.CustomSubError} entries for each invalid field.
     *
     * @param ex the thrown {@link MethodArgumentNotValidException}
     * @return a {@link ResponseEntity} containing a {@link CustomError} and HTTP 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex) {

        List<CustomError.CustomSubError> subErrors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(
                error -> {
                    String fieldName = error instanceof FieldError fe ? fe.getField() : error.getObjectName();
                    String message = messageSource.getMessage(error, locale());
                    subErrors.add(
                            CustomError.CustomSubError.builder()
                                    .field(fieldName)
                                    .message(message)
                                    .build()
                    );
                }
        );

        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message(msg("validation.failed"))
                .subErrors(subErrors)
                .build();

        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles constraint violations on path variables or request parameters.
     * Transforms {@link ConstraintViolationException} into a {@link CustomError}
     * with {@link CustomError.Header#VALIDATION_ERROR} and one
     * {@link CustomError.CustomSubError} per violated constraint.
     *
     * @param ex the thrown {@link ConstraintViolationException}
     * @return a {@link ResponseEntity} containing a {@link CustomError} and HTTP 400
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handlePathVariableErrors(final ConstraintViolationException ex) {

        List<CustomError.CustomSubError> subErrors = new ArrayList<>();
        ex.getConstraintViolations()
                .forEach(constraintViolation -> {
                            Object invalidValue = constraintViolation.getInvalidValue();
                            String propertyPath = constraintViolation.getPropertyPath().toString();
                            String fieldName = propertyPath.contains(".")
                                    ? propertyPath.substring(propertyPath.lastIndexOf('.') + 1)
                                    : propertyPath;

                            subErrors.add(
                                    CustomError.CustomSubError.builder()
                                            .message(constraintViolation.getMessage())
                                            .field(fieldName)
                                            .value(invalidValue != null ? invalidValue.toString() : null)
                                            .type(invalidValue != null ? invalidValue.getClass().getSimpleName() : null)
                                            .build()
                            );
                        }
                );

        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message(msg("constraint.violation"))
                .subErrors(subErrors)
                .build();

        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<Object> handleBindException(final BindException ex) {

        List<CustomError.CustomSubError> subErrors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = (error instanceof FieldError fe) ? fe.getField() : error.getObjectName();
            String message = messageSource.getMessage(error, locale());

            subErrors.add(CustomError.CustomSubError.builder()
                    .field(fieldName)
                    .message(message)
                    .build());
        });

        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message(msg("bind.failed"))
                .subErrors(subErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(customError);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<Object> handleNotReadable(final HttpMessageNotReadableException ex) {

        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message(msg("message.not.readable"))
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(customError);
    }

    /**
     * Handles missing multipart parts (e.g. missing {@code file} part in a multipart request).
     * Converts {@link MissingServletRequestPartException} into a {@link CustomError}
     * with {@link CustomError.Header#API_ERROR}.
     *
     * @param ex the thrown {@link MissingServletRequestPartException}
     * @return a {@link ResponseEntity} containing a {@link CustomError} and HTTP 400
     */
    @ExceptionHandler(MissingServletRequestPartException.class)
    protected ResponseEntity<Object> handleMissingPart(final MissingServletRequestPartException ex) {

        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.API_ERROR.getName())
                .message(msg("request.part.missing", ex.getRequestPartName()))
                .build();

        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<Object> handleMissingParam(final MissingServletRequestParameterException ex) {

        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message(msg("request.param.missing", ex.getParameterName()))
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(customError);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleTypeMismatch(final MethodArgumentTypeMismatchException ex) {

        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message(msg("type.mismatch", ex.getName()))
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(customError);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<Object> handleMethodNotSupported(final HttpRequestMethodNotSupportedException ex) {

        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.METHOD_NOT_ALLOWED)
                .header(CustomError.Header.API_ERROR.getName())
                .message(msg("method.not.supported"))
                .build();

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(customError);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<Object> handleNoHandlerFound(final NoHandlerFoundException ex) {

        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .header(CustomError.Header.API_ERROR.getName())
                .message(msg("no.handler", ex.getHttpMethod(), ex.getRequestURL()))
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(customError);
    }

    /**
     * Fallback handler for any unhandled exceptions.
     * Acts as a safety net for unexpected errors, converting them into a generic
     * {@link CustomError} with {@link CustomError.Header#API_ERROR}.
     *
     * @param ex the thrown {@link Exception}
     * @return a {@link ResponseEntity} containing a {@link CustomError} and HTTP 500
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleGeneralException(final Exception ex) {

        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(CustomError.Header.API_ERROR.getName())
                .message(msg("unexpected.error"))
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(customError);
    }

    /**
     * Handles all custom {@link ApiException} subclasses.
     * Uses {@link ApiException#getStatus()} and {@link ApiException#getHeader()}
     * to build a corresponding {@link CustomError} response.
     *
     * @param ex the thrown {@link ApiException}
     * @return a {@link ResponseEntity} containing a {@link CustomError}
     * and the status provided by {@link ApiException#getStatus()}
     */
    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<Object> handleApiException(final ApiException ex) {

        CustomError customError = CustomError.builder()
                .httpStatus(ex.getStatus())
                .header(ex.getHeader().getName())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(customError, ex.getStatus());
    }

}



