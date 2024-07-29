package com.unicorn.store.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private final ErrorCode errorCode;

}
