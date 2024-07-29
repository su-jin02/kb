package com.unicorn.store.exceptions.token;

import com.unicorn.store.exceptions.CustomException;
import com.unicorn.store.exceptions.ErrorCode;

public class ExpiredJwtTokenException extends CustomException {
    public ExpiredJwtTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
