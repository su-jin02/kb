package com.unicorn.store.handler.exception.http;

import com.unicorn.store.handler.exception.CustomException;
import com.unicorn.store.handler.exception.ErrorCode;

public class CustomForbiddenException extends CustomException {
    public CustomForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
