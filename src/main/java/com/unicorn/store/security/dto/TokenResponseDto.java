package com.unicorn.store.security.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenResponseDto {
    String tokenType;
    String accessToken;
    Long expiresIn;
    String refreshToken;
    Long refreshTokenExpiresIn;
    Long userId;
    String nickname;

    // 기본 생성자
    public TokenResponseDto() {
    }

    // 모든 필드를 포함하는 생성자
    @JsonCreator
    public TokenResponseDto(
            @JsonProperty("tokenType") String tokenType,
            @JsonProperty("accessToken") String accessToken,
            @JsonProperty("expiresIn") Long expiresIn,
            @JsonProperty("refreshToken") String refreshToken,
            @JsonProperty("refreshTokenExpiresIn") Long refreshTokenExpiresIn,
            @JsonProperty("userId") Long userId,
            @JsonProperty("nickname") String nickname) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
        this.userId = userId;
        this.nickname = nickname;
    }
}
