package com.unicorn.store.handler.exception.token;

import com.unicorn.store.handler.exception.CustomException;
import com.unicorn.store.handler.exception.ErrorCode;

public class ExpiredTokenException extends CustomException {
    public ExpiredTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
