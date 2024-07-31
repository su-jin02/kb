package com.unicorn.store.dto.user;

import com.unicorn.store.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserReq {
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SignupAndLogin {

        @NotNull
        @Schema(required = true,description = "유저 닉네임")
        String nickname;

        @NotNull
        @Schema(required = true, description = "유저 기기 번호")
        String password;

        public User toEntity(String encPassword){
            return User.builder()
                    .nickname(nickname)
                    .password(encPassword)
                    .build();
        }
    }

    @Getter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SocialLogin {

        @NotNull
        String email;

        @NotNull
        String password;

        @NotNull
        String nickname;

        public User toEntity(){
            return User.builder()
                    .email(email)
                    .nickname(nickname)
                    .password(password)
                    .build();
        }
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ChangeNickname {

        @NotNull
        String nickname;
    }

    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Token {

        @NotNull
        String refreshToken;
    }
}
