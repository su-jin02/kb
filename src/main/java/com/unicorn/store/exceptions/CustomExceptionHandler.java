package com.unicorn.store.exceptions;

import com.unicorn.store.dto.common.CMRes;
import com.unicorn.store.dto.common.ErrorRes;
import com.unicorn.store.exceptions.http.*;
import com.unicorn.store.exceptions.token.ExpiredJwtTokenException;
import com.unicorn.store.exceptions.token.ExpiredTokenException;
import com.unicorn.store.exceptions.token.InvalidTokenException;
import com.unicorn.store.security.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.UnexpectedTypeException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.sql.SQLIntegrityConstraintViolationException;

import static com.unicorn.store.exceptions.ErrorCode.*;

/**
 * NAME: CustomExceptionHandler 를 이용해 예외처리
 */

@RequiredArgsConstructor
@RestControllerAdvice
public class CustomExceptionHandler {
    private final TokenProvider tokenProvider;

    /* request @Valid 유효성 체크 통과하지 못할 시 실행됨 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRes> handleValidException(MethodArgumentNotValidException ex) {

        return new ResponseEntity<>(new ErrorRes(-1, BAD_REQUEST.getStatus(), ex.getBindingResult().getFieldError().getDefaultMessage()), HttpStatus.valueOf(400));
    }

    /* request @Valid 유효성 체크 통과하지 못할 시 실행됨 */
    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<ErrorRes> handleValidException(ValidationException ex) {
        return new ResponseEntity<>(new ErrorRes(-1, FAILED_VALIDATION.getStatus(), FAILED_VALIDATION.getMessage()), HttpStatus.valueOf(400));
    }

    /* 필수 request 오지 않을 때 실행됨 */
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorRes> handleRequiredRequest(MissingServletRequestPartException ex) {
        return new ResponseEntity<>(new ErrorRes(-1, 400, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * 권한 없을 때
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorRes> accessDeniedException(HttpServletRequest httpServletRequest, AccessDeniedException e) {
        try {
            String jwtToken = tokenProvider.getJwtToken(httpServletRequest);
            tokenProvider.isValidToken(jwtToken);
        } catch (ExpiredJwtTokenException ex) {
            return new ResponseEntity<>(new ErrorRes(-1, ErrorCode.EXPIRED_TOKEN.getStatus(), ErrorCode.EXPIRED_TOKEN.getMessage()), HttpStatus.FORBIDDEN);
        } catch (ExpiredTokenException | InvalidTokenException ex) {
            return new ResponseEntity<>(new ErrorRes(-1, ErrorCode.INVALID_TOKEN.getStatus(), ErrorCode.INVALID_TOKEN.getMessage()), HttpStatus.FORBIDDEN);
        } catch (Exception ignored) {
        }
        return new ResponseEntity<>(new ErrorRes(-1, ErrorCode.UNAUTHORIZED.getStatus(), ErrorCode.UNAUTHORIZED.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    /**
     * 만료된 토큰
     */
    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<ErrorRes> expiredTokenExceptionHandler(ExpiredTokenException e) {
        return new ResponseEntity<>(new ErrorRes(-1, e.getErrorCode().getStatus(), e.getErrorCode().getMessage()), HttpStatus.FORBIDDEN);
    }

    /**
     * 유효하지 않은 토큰
     */
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorRes> invalidTokenExceptionHandler(InvalidTokenException e) {
        return new ResponseEntity<>(new ErrorRes(-1, e.getErrorCode().getStatus(), e.getErrorCode().getMessage()), HttpStatus.FORBIDDEN);
    }

    //유효성 검사
    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<CMRes<?>> validationApiExceptionHandler(CustomValidationException e) {
        return new ResponseEntity<>(new CMRes<>(-1, e.getMessage(), e.errorMap), HttpStatus.BAD_REQUEST);
    }

    //sql 에러
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<CMRes<?>> apiSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        return new ResponseEntity<>(new CMRes<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    /**
     * 400 (잘못된 요청)
     */
    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<ErrorRes> badRequestExceptionHandler(CustomBadRequestException e) {
        return new ResponseEntity<>(new ErrorRes(-1, e.getErrorCode().getStatus(), e.getErrorCode().getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * 403 (권한없음)
     */
    @ExceptionHandler(CustomForbiddenException.class)
    public ResponseEntity<ErrorRes> forbiddenExceptionHandler(CustomForbiddenException e) {
        return new ResponseEntity<>(new ErrorRes(-1, e.getErrorCode().getStatus(), e.getErrorCode().getMessage()), HttpStatus.FORBIDDEN);
    }

    /**
     * 404 (Not Found)
     */
    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<ErrorRes> notFoundExceptionHandler(CustomNotFoundException e) {
        return new ResponseEntity<>(new ErrorRes(-1, e.getErrorCode().getStatus(), e.getErrorCode().getMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * 409 (충돌)
     */
    @ExceptionHandler(CustomConflictException.class)
    public ResponseEntity<ErrorRes> conflictExceptionHandler(CustomConflictException e) {
        return new ResponseEntity<>(new ErrorRes(-1, e.getErrorCode().getStatus(), e.getErrorCode().getMessage()), HttpStatus.CONFLICT);
    }

    /**
     * 서버 500 내부 에러
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorRes> ServerExceptionHandler(Exception ex) {
        return new ResponseEntity<>(new ErrorRes(-1, INTERNAL_SERVER_ERROR.getStatus(), INTERNAL_SERVER_ERROR.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
