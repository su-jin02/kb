package com.unicorn.store.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum BaseResponseStatus {
    OK(1,HttpStatus.OK, "요청에 성공하였습니다."),
    CREATED(1, HttpStatus.CREATED, "요청에 따른 리소스 생성에 성공하였습니다."),

    INTERNAL_SERVER_ERROR(-1, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류 입니다");

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;
}
