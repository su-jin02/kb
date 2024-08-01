package com.unicorn.store.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicorn.store.response.BaseResponse;
import com.unicorn.store.response.BaseResponseStatus;
import com.unicorn.store.dto.common.ErrorRes;
import com.unicorn.store.dto.user.UserReq;
import com.unicorn.store.security.dto.TokenResponseDto;
import com.unicorn.store.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Tag(name = "user", description = "유저 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 회원가입 & 로그인
     */
    @Operation(summary = "일반 사용자 회원가입 & 로그인", description = "일반 사용자 회원가입 & 로그인 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일반 유저 회원가입 & 로그인 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "비밀번호 오류", content = @Content(schema = @Schema(implementation = ErrorRes.class)))
    })
    @PostMapping(value = "/login")
    public BaseResponse<TokenResponseDto> signup (@Parameter(description = "일반 로그인/회원가입 요청 객체") @Valid @RequestBody UserReq.SignupAndLogin SignupAndLogin) {
        return BaseResponse.success(BaseResponseStatus.OK, userService.SignupAndLogin(SignupAndLogin));
    }

    /**
     * 카카오로그인
     */
    @Operation(summary = "소셜로그인", description = "카카오 로그인/회원가입 API 입니다.")
    @GetMapping(value = "/login/oauth2/code/kakao", produces = "application/json")
    public ModelAndView kakaoCallback(@RequestParam String code, RedirectAttributes redirectAttributes) throws JsonProcessingException {
        TokenResponseDto tokenResponse = userService.socialLogin(code);

        // Convert tokenResponse to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String tokenResponseJson = objectMapper.writeValueAsString(tokenResponse);

        // Add tokenResponseJson as a flash attribute
        redirectAttributes.addFlashAttribute("tokenResponseJson", tokenResponseJson);

        // Redirect to home
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/board")
    public ModelAndView home(@ModelAttribute("tokenResponseJson") String tokenResponseJson) throws JsonProcessingException {
        ModelAndView modelAndView = new ModelAndView("board");

        if (tokenResponseJson != null) {
            // Convert JSON string back to TokenResponseDto
            ObjectMapper objectMapper = new ObjectMapper();
            TokenResponseDto tokenResponse = objectMapper.readValue(tokenResponseJson, TokenResponseDto.class);
            modelAndView.addObject("nickname", tokenResponse.getNickname());
        }

        return modelAndView;
    }

    /**
     * 닉네임 변경
     */
    @Operation(summary = "닉네임 변경", description = "닉네임 변경 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임 변경 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 저장된 닉네임", content = @Content(schema = @Schema(implementation = ErrorRes.class)))
    })
    @PatchMapping(value = "/nickname", produces = "application/json")
    public BaseResponse changeNickname(@Parameter(description = "닉네임 변경 요청 객체") @Valid @RequestBody UserReq.ChangeNickname changeNickname) {
        userService.changeNickname(changeNickname);
        return BaseResponse.success(BaseResponseStatus.OK);
    }

    /**
     * 회원 탈퇴
     */
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @DeleteMapping(value ="/user")
    public BaseResponse deleteUser() {
        userService.deleteUser();
        return BaseResponse.success(BaseResponseStatus.OK);
    }

    /**
     * AccessToken 재발급
     */
    @Operation(summary = "AccessToken 재발급", description = "만료 또는 새로고침으로 인한 AccessToken 재발급 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "AccessToken 재발급 성공", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 RefreshToken", content = @Content(schema = @Schema(implementation = ErrorRes.class)))
    })
    @PostMapping("/token")
    public BaseResponse<String> accessTokenRefresh(@Valid @RequestBody UserReq.Token request) {
        return BaseResponse.success(BaseResponseStatus.OK, userService.getAccessToken(request.getRefreshToken()));
    }
}
