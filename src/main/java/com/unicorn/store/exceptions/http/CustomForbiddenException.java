package com.unicorn.store.exceptions.http;

import com.unicorn.store.exceptions.CustomException;
import com.unicorn.store.exceptions.ErrorCode;

public class CustomForbiddenException extends CustomException {
    public CustomForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
