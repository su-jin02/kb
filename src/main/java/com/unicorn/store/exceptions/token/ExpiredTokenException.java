package com.unicorn.store.exceptions.token;

import com.unicorn.store.exceptions.CustomException;
import com.unicorn.store.exceptions.ErrorCode;

public class ExpiredTokenException extends CustomException {
    public ExpiredTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
