package com.unicorn.store.handler.exception.http;

import com.unicorn.store.handler.exception.CustomException;
import com.unicorn.store.handler.exception.ErrorCode;

public class CustomBadRequestException extends CustomException {
    public CustomBadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
