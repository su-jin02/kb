package com.unicorn.store.exceptions.http;

import com.unicorn.store.exceptions.CustomException;
import com.unicorn.store.exceptions.ErrorCode;

public class CustomNotFoundException extends CustomException {
    public CustomNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

}
