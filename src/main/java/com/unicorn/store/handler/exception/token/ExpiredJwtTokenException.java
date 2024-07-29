package com.unicorn.store.handler.exception.token;

import com.unicorn.store.handler.exception.CustomException;
import com.unicorn.store.handler.exception.ErrorCode;

public class ExpiredJwtTokenException extends CustomException {
    public ExpiredJwtTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
