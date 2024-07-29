package com.unicorn.store.exceptions.http;

import com.unicorn.store.exceptions.CustomException;
import com.unicorn.store.exceptions.ErrorCode;

public class CustomBadRequestException extends CustomException {
    public CustomBadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
