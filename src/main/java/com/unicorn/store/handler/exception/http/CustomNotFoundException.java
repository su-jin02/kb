package com.unicorn.store.handler.exception.http;

import com.unicorn.store.handler.exception.CustomException;
import com.unicorn.store.handler.exception.ErrorCode;

public class CustomNotFoundException extends CustomException {
    public CustomNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

}
