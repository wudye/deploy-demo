package com.mwu.geodistance.common.exception;

import com.mwu.geodistance.base.AbstractBaseServiceTest;
import com.mwu.geodistance.common.exception.error.CustomError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest extends AbstractBaseServiceTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private MessageSource messageSource;

    @AfterEach
    void tearDown() {
        LocaleContextHolder.resetLocaleContext();
    }

    @Test
    void handleMethodArgumentNotValid_returnsBadRequest_withSubErrors() {

        // Given
        LocaleContextHolder.setLocale(Locale.ENGLISH);

        BindingResult bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        FieldError fe = new FieldError("obj", "age", "must be >= 18");
        when(bindingResult.getAllErrors()).thenReturn(List.of(fe));

        // per-field message comes from: messageSource.getMessage(error, locale)
        when(messageSource.getMessage(eq(fe), any(Locale.class))).thenReturn("must be >= 18");

        // top message comes from: msg("validation.failed")
        when(messageSource.getMessage(eq("validation.failed"), any(Object[].class), eq("validation.failed"), any(Locale.class)))
                .thenReturn("Validation failed");

        // When
        ResponseEntity<Object> resp = globalExceptionHandler.handleMethodArgumentNotValid(ex);

        // Then
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        CustomError expected = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message("Validation failed")
                .subErrors(List.of(CustomError.CustomSubError.builder()
                        .field("age")
                        .message("must be >= 18")
                        .build()))
                .build();

        checkCustomError(expected, (CustomError) resp.getBody());
    }

    @Test
    void handlePathVariableErrors_returnsBadRequest_withMappedViolations_whenPathHasDot() {

        // Given
        LocaleContextHolder.setLocale(Locale.ENGLISH);

        @SuppressWarnings("unchecked")
        ConstraintViolation<Object> cv = mock(ConstraintViolation.class);
        jakarta.validation.Path path = mock(jakarta.validation.Path.class);

        when(cv.getMessage()).thenReturn("must be positive");
        when(cv.getPropertyPath()).thenReturn(path);
        when(path.toString()).thenReturn("dto.amount");
        when(cv.getInvalidValue()).thenReturn(-5);

        ConstraintViolationException ex = new ConstraintViolationException(Set.of(cv));

        when(messageSource.getMessage(eq("constraint.violation"), any(Object[].class), eq("constraint.violation"), any(Locale.class)))
                .thenReturn("Constraint violation");

        // When
        ResponseEntity<Object> resp = globalExceptionHandler.handlePathVariableErrors(ex);

        // Then
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        CustomError expected = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message("Constraint violation")
                .subErrors(List.of(CustomError.CustomSubError.builder()
                        .message("must be positive")
                        .field("amount")
                        .value("-5")
                        .type("Integer")
                        .build()))
                .build();

        checkCustomError(expected, (CustomError) resp.getBody());
    }

    @Test
    void handlePathVariableErrors_whenPathHasNoDot_andInvalidValueIsNull_setsFieldAndNullValueAndType() {

        // Given
        LocaleContextHolder.setLocale(Locale.ENGLISH);

        @SuppressWarnings("unchecked")
        ConstraintViolation<Object> cv = mock(ConstraintViolation.class);
        jakarta.validation.Path path = mock(jakarta.validation.Path.class);

        when(cv.getMessage()).thenReturn("must not be blank");
        when(cv.getPropertyPath()).thenReturn(path);
        when(path.toString()).thenReturn("username");
        when(cv.getInvalidValue()).thenReturn(null);

        ConstraintViolationException ex = new ConstraintViolationException(Set.of(cv));

        when(messageSource.getMessage(eq("constraint.violation"), any(Object[].class), eq("constraint.violation"), any(Locale.class)))
                .thenReturn("Constraint violation");

        // When
        ResponseEntity<Object> resp = globalExceptionHandler.handlePathVariableErrors(ex);

        // Then
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        CustomError expected = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message("Constraint violation")
                .subErrors(List.of(CustomError.CustomSubError.builder()
                        .message("must not be blank")
                        .field("username")
                        .value(null)
                        .type(null)
                        .build()))
                .build();

        checkCustomError(expected, (CustomError) resp.getBody());
    }

    @Test
    void handleBindException_returnsBadRequest_withSubErrors() {

        // Given
        LocaleContextHolder.setLocale(Locale.ENGLISH);

        BindException ex = mock(BindException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        FieldError fe = new FieldError("obj", "name", "must not be blank");
        when(bindingResult.getAllErrors()).thenReturn(List.of(fe));

        when(messageSource.getMessage(eq(fe), any(Locale.class))).thenReturn("must not be blank");
        when(messageSource.getMessage(eq("bind.failed"), any(Object[].class), eq("bind.failed"), any(Locale.class)))
                .thenReturn("Bind failed");

        // When
        ResponseEntity<Object> resp = globalExceptionHandler.handleBindException(ex);

        // Then
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        CustomError expected = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message("Bind failed")
                .subErrors(List.of(CustomError.CustomSubError.builder()
                        .field("name")
                        .message("must not be blank")
                        .build()))
                .build();

        checkCustomError(expected, (CustomError) resp.getBody());
    }

    @Test
    void handleNotReadable_returnsBadRequest_withValidationHeader() {

        // Given
        LocaleContextHolder.setLocale(Locale.ENGLISH);

        HttpInputMessage inputMessage = mock(HttpInputMessage.class);
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("invalid json", inputMessage);

        when(messageSource.getMessage(eq("message.not.readable"), any(Object[].class), eq("message.not.readable"), any(Locale.class)))
                .thenReturn("Malformed request body");

        // When
        ResponseEntity<Object> resp = globalExceptionHandler.handleNotReadable(ex);

        // Then
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        CustomError expected = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message("Malformed request body")
                .build();

        checkCustomError(expected, (CustomError) resp.getBody());
    }

    @Test
    void handleMissingPart_returnsBadRequest_withApiErrorHeader() {

        // Given
        LocaleContextHolder.setLocale(Locale.ENGLISH);

        MissingServletRequestPartException ex = new MissingServletRequestPartException("file");

        // msg("request.part.missing", ex.getRequestPartName())
        when(messageSource.getMessage(eq("request.part.missing"), any(Object[].class), eq("request.part.missing"), any(Locale.class)))
                .thenReturn("Required request part is missing: file");

        // When
        ResponseEntity<Object> resp = globalExceptionHandler.handleMissingPart(ex);

        // Then
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        CustomError expected = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.API_ERROR.getName())
                .message("Required request part is missing: file")
                .build();

        checkCustomError(expected, (CustomError) resp.getBody());
    }

    @Test
    void handleMissingParam_returnsBadRequest_withValidationHeader() throws Exception {

        // Given
        LocaleContextHolder.setLocale(Locale.ENGLISH);

        MissingServletRequestParameterException ex =
                new MissingServletRequestParameterException("page", "int");

        when(messageSource.getMessage(eq("request.param.missing"), any(Object[].class), eq("request.param.missing"), any(Locale.class)))
                .thenReturn("Required request parameter is missing: page");

        // When
        ResponseEntity<Object> resp = globalExceptionHandler.handleMissingParam(ex);

        // Then
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        CustomError expected = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message("Required request parameter is missing: page")
                .build();

        checkCustomError(expected, (CustomError) resp.getBody());
    }

    @Test
    void handleTypeMismatch_returnsBadRequest_withValidationHeader_andParameterName() {

        // Given
        LocaleContextHolder.setLocale(Locale.ENGLISH);

        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getName()).thenReturn("id");

        when(messageSource.getMessage(eq("type.mismatch"), any(Object[].class), eq("type.mismatch"), any(Locale.class)))
                .thenReturn("Invalid value for parameter: id");

        // When
        ResponseEntity<Object> resp = globalExceptionHandler.handleTypeMismatch(ex);

        // Then
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        CustomError expected = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message("Invalid value for parameter: id")
                .build();

        checkCustomError(expected, (CustomError) resp.getBody());
    }

    @Test
    void handleMethodNotSupported_returnsMethodNotAllowed_withApiErrorHeader() {

        // Given
        LocaleContextHolder.setLocale(Locale.ENGLISH);

        HttpRequestMethodNotSupportedException ex = mock(HttpRequestMethodNotSupportedException.class);

        when(messageSource.getMessage(eq("method.not.supported"), any(Object[].class), eq("method.not.supported"), any(Locale.class)))
                .thenReturn("Method not supported");

        // When
        ResponseEntity<Object> resp = globalExceptionHandler.handleMethodNotSupported(ex);

        // Then
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);

        CustomError expected = CustomError.builder()
                .httpStatus(HttpStatus.METHOD_NOT_ALLOWED)
                .header(CustomError.Header.API_ERROR.getName())
                .message("Method not supported")
                .build();

        checkCustomError(expected, (CustomError) resp.getBody());
    }

    @Test
    void handleNoHandlerFound_returnsNotFound_withApiErrorHeader() {

        // Given
        LocaleContextHolder.setLocale(Locale.ENGLISH);

        NoHandlerFoundException ex = mock(NoHandlerFoundException.class);
        when(ex.getHttpMethod()).thenReturn("GET");
        when(ex.getRequestURL()).thenReturn("/api/unknown");

        when(messageSource.getMessage(eq("no.handler"), any(Object[].class), eq("no.handler"), any(Locale.class)))
                .thenReturn("No handler for GET /api/unknown");

        // When
        ResponseEntity<Object> resp = globalExceptionHandler.handleNoHandlerFound(ex);

        // Then
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        CustomError expected = CustomError.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .header(CustomError.Header.API_ERROR.getName())
                .message("No handler for GET /api/unknown")
                .build();

        checkCustomError(expected, (CustomError) resp.getBody());
    }

    @Test
    void handleGeneralException_returnsInternalServerError_withApiErrorHeader() {

        // Given
        LocaleContextHolder.setLocale(Locale.ENGLISH);

        Exception ex = new Exception("boom");

        when(messageSource.getMessage(eq("unexpected.error"), any(Object[].class), eq("unexpected.error"), any(Locale.class)))
                .thenReturn("Unexpected error occurred");

        // When
        ResponseEntity<Object> resp = globalExceptionHandler.handleGeneralException(ex);

        // Then
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

        CustomError expected = CustomError.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(CustomError.Header.API_ERROR.getName())
                .message("Unexpected error occurred")
                .build();

        checkCustomError(expected, (CustomError) resp.getBody());
    }

    @Test
    void handleApiException_returnsMappedStatusAndHeader() {

        // Given
        ApiException ex = mock(ApiException.class);
        when(ex.getStatus()).thenReturn(HttpStatus.UNPROCESSABLE_ENTITY);
        when(ex.getHeader()).thenReturn(CustomError.Header.PROCESS_ERROR);
        when(ex.getMessage()).thenReturn("Custom api error");

        // When
        ResponseEntity<Object> resp = globalExceptionHandler.handleApiException(ex);

        // Then
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

        CustomError expected = CustomError.builder()
                .httpStatus(HttpStatus.UNPROCESSABLE_ENTITY)
                .header(CustomError.Header.PROCESS_ERROR.getName())
                .message("Custom api error")
                .build();

        checkCustomError(expected, (CustomError) resp.getBody());
    }

    private void checkCustomError(CustomError expectedError, CustomError actualError) {

        assertThat(actualError).isNotNull();
        assertThat(actualError.getTime()).isNotNull();
        assertThat(actualError.getHeader()).isEqualTo(expectedError.getHeader());
        assertThat(actualError.getIsSuccess()).isEqualTo(expectedError.getIsSuccess());

        if (expectedError.getMessage() != null) {
            assertThat(actualError.getMessage()).isEqualTo(expectedError.getMessage());
        }

        if (expectedError.getSubErrors() != null) {
            assertThat(actualError.getSubErrors().size()).isEqualTo(expectedError.getSubErrors().size());
            if (!expectedError.getSubErrors().isEmpty()) {
                assertThat(actualError.getSubErrors().getFirst().getMessage()).isEqualTo(expectedError.getSubErrors().get(0).getMessage());
                assertThat(actualError.getSubErrors().getFirst().getField()).isEqualTo(expectedError.getSubErrors().get(0).getField());
                assertThat(actualError.getSubErrors().getFirst().getValue()).isEqualTo(expectedError.getSubErrors().get(0).getValue());
                assertThat(actualError.getSubErrors().getFirst().getType()).isEqualTo(expectedError.getSubErrors().get(0).getType());
            }
        }

    }

}