package com.mwu.geodistance.common.exception.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standardized error payload for API responses.
 * Used by global exception handlers to return a consistent structure
 * (status, message, and optional field-level sub-errors) on failures.
 */
@Getter
@Builder
public class CustomError {

    // 该注解用于在使用 Lombok 的 @Builder 注解生成对象时，为字段提供默认值。
    // 如果未显式设置该字段的值，time 将自动初始化为默认值。
    // 确保未设置的字段有默认值
    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();

    private HttpStatus httpStatus;

    private String header;

    // 表示只有当 message 字段的值不为 null 时，才会将其包含在生成的 JSON 中。如果字段值为 null，则会被忽略。
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @Builder.Default
    private final Boolean isSuccess = false;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CustomSubError> subErrors;

    /**
     * Represents a sub-error, usually used for validation problems.
     */
    @Getter
    @Builder
    public static class CustomSubError {

        private String message;

        private String field;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Object value;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String type;

    }

    /**
     * Enumeration of predefined error categories for consistent API responses.
     */
    @Getter
    @RequiredArgsConstructor
    public enum Header {

        API_ERROR("API ERROR"),

        VALIDATION_ERROR("VALIDATION ERROR"),

        PROCESS_ERROR("PROCESS ERROR");

        private final String name;

    }

}