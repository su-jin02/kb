package com.unicorn.store.exceptions.http;

import com.unicorn.store.exceptions.CustomException;
import com.unicorn.store.exceptions.ErrorCode;

public class CustomConflictException extends CustomException {
    public CustomConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}
