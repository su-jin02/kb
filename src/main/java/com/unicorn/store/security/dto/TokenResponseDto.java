package com.unicorn.store.security.dto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenResponseDto {
    final String tokenType;

    final String accessToken;

    final Long expiresIn;

    final String refreshToken;

    final Long refreshTokenExpiresIn;

    final Long userId;

    final String nickname;
}
