package com.unicorn.store.exceptions.http;

import java.util.Map;

public class CustomValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public Map<String, String> errorMap;

    public CustomValidationException(String message, Map<String, String> errorMap) {
        super(message);
        this.errorMap = errorMap;
    }
}
