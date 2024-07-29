package com.unicorn.store.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CMRes <T> {
    private int Code; //1(성공), -1(실패)
    private String message;
    private T data;
}
