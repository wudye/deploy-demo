package com.easybank.accounts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// @ResponseStatus(value = HttpStatus.NOT_FOUND) 是 Spring 提供的注解，用于指定当该异常被抛出时，返回的 HTTP 状态码为 404。这使得在 REST API 中，当资源未找到时，可以通过抛出该异常来自动返回适当的状态码，而无需手动设置响应。

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s not found with the given input data %s : '%s'", resourceName, fieldName, fieldValue));
    }

}
