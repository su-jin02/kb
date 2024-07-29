package com.unicorn.store.handler.exception.http;

import com.unicorn.store.handler.exception.CustomException;
import com.unicorn.store.handler.exception.ErrorCode;

public class CustomConflictException extends CustomException {
    public CustomConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}
