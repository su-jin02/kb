package com.unicorn.store.service;

import com.unicorn.store.exceptions.ErrorCode;
import com.unicorn.store.exceptions.http.CustomForbiddenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {
    public Long getLoginUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("-------------------");
        log.info(String.valueOf(authentication));
        log.info("-------------------");
        if (UsernamePasswordAuthenticationToken.class.isAssignableFrom(
                Optional.ofNullable(authentication)
                        .orElseThrow(() -> new CustomForbiddenException(ErrorCode.FORBIDDEN)).getClass())) {
            return Long.valueOf(authentication.getName());
        }
        return 0L;
    }
}
