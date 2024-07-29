package com.unicorn.store.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userId;

    String nickname;

    String email;

    String password;
    String jwt;

    public void changeNickname(String nickname){
        this.nickname = nickname;
    }

    public void changeJwtToken(String refreshToken) {
        this.jwt = refreshToken;
    }

}
