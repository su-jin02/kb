package com.unicorn.store.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorRes {
    private int Code; //1(성공), -1(실패)
    private int status;
    private String message;
}
