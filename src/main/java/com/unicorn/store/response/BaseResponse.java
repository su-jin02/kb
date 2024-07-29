package com.unicorn.store.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@NoArgsConstructor

@JsonPropertyOrder({"code", "httpStatus", "message", "data"}) // Json 으로 나갈 순서를 설정하는 어노테이션
@JsonInclude(JsonInclude.Include.NON_NULL) // Json으로 응답이 나갈 때 - null인 필드는(CLASS LEVEL에 붙었으니) 응답으로 포함시키지 않는 어노테이션
public class BaseResponse <T> {
    @JsonProperty("code")
    private int code;

    @JsonProperty("httpStatus")
    private HttpStatus httpStatus;

    private String message;

    private T data;

    private T invalidInput;

    /** [정적 메서드 팩토리 패턴] */
    // (result 필드는 어차피 null 초기값이 그대로 있어서 - Json으로 변환시 나가지 않을 것)
    private BaseResponse(int code, HttpStatus httpStatus, String message) {

        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    private BaseResponse(int code, HttpStatus httpStatus, String message, T data) {

        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
        this.data = data;
    }

    private BaseResponse(BaseResponseStatus status, T invalidInput) {

        this.code = status.getCode();
        this.httpStatus = status.getHttpStatus();
        this.message = status.getMessage();
        this.invalidInput = invalidInput;
    }

    /** API 성공시 나가는 응답 */
    public static BaseResponse success(BaseResponseStatus status) {

        return new BaseResponse<>(status.getCode(), status.getHttpStatus(), status.getMessage());
    }

    public static <T> BaseResponse<T> success(BaseResponseStatus status, T data){

        return new BaseResponse<T>(status.getCode(), status.getHttpStatus(), status.getMessage(), data);
    }

    /** API 실패시 나가는 응답 */
    public static BaseResponse fail(BaseResponseStatus status){
        return new BaseResponse<>(status.getCode(), status.getHttpStatus(), status.getMessage());
    }

    public static <T> BaseResponse<T> fail(BaseResponseStatus status, T invalidInput){
        return new BaseResponse<T>(status, invalidInput);
    }
}
