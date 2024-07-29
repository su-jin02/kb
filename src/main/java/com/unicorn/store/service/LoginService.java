package com.unicorn.store.service;

import com.unicorn.store.handler.exception.ErrorCode;
import com.unicorn.store.handler.exception.http.CustomForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class LoginService {
    public Long getLoginUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UsernamePasswordAuthenticationToken.class.isAssignableFrom(
                Optional.ofNullable(authentication)
                        .orElseThrow(() -> new CustomForbiddenException(ErrorCode.FORBIDDEN)).getClass())) {
            return Long.valueOf(authentication.getName());
        }
        return 0L;
    }
}
