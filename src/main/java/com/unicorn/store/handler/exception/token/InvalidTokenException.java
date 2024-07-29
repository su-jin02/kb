package com.unicorn.store.handler.exception.token;

import com.unicorn.store.handler.exception.CustomException;
import com.unicorn.store.handler.exception.ErrorCode;

public class InvalidTokenException extends CustomException {
    public InvalidTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}