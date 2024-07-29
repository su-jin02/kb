package com.unicorn.store.exceptions.token;

import com.unicorn.store.exceptions.CustomException;
import com.unicorn.store.exceptions.ErrorCode;

public class InvalidTokenException extends CustomException {
    public InvalidTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}