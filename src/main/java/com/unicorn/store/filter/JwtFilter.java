package com.unicorn.store.filter;
import com.unicorn.store.security.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            String jwtToken = tokenProvider.getJwtToken(request);
            log.info("-------jwtToken------------");
            log.info(String.valueOf(jwtToken));
            log.info("-------------------");

            if (!ObjectUtils.isEmpty(jwtToken) && tokenProvider.isValidToken(jwtToken)) {
                log.info("-------if------------");
                log.info(String.valueOf(jwtToken));
                log.info("-------------------");
                Authentication authentication = tokenProvider.getAuthentication(jwtToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ignore){
        }

        filterChain.doFilter(request, response);
    }
}
