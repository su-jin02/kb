package com.unicorn.store.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicorn.store.model.User;
import com.unicorn.store.dto.user.UserReq;
import com.unicorn.store.handler.exception.ErrorCode;
import com.unicorn.store.handler.exception.http.CustomBadRequestException;
import com.unicorn.store.handler.exception.http.CustomConflictException;
import com.unicorn.store.handler.exception.http.CustomNotFoundException;
import com.unicorn.store.data.UserRepository;
import com.unicorn.store.security.TokenProvider;
import com.unicorn.store.security.dto.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final LoginService loginService;

    /**
     * 사용자 닉네임 중복 검사
     */
    public boolean isDuplicateNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    /**
     * 회원가입 & 로그인
     */
    @Transactional
    public TokenResponseDto SignupAndLogin(UserReq.SignupAndLogin userSignupAndLogin) {
        User user = null;
        if (isDuplicateNickname(userSignupAndLogin.getNickname())) {
            user = userRepository.findByNickname(userSignupAndLogin.getNickname());
            if (!passwordEncoder.matches(userSignupAndLogin.getPassword(), user.getPassword())) {
                throw new CustomBadRequestException(ErrorCode.ALREADY_SAVED_NICKNAME);
            }
        }

        else{
            String rawPassword = userSignupAndLogin.getPassword();
            String encPassword = passwordEncoder.encode(rawPassword);
            user = userSignupAndLogin.toEntity(encPassword);
            userRepository.save(user);
        }

        TokenResponseDto tokenResponseDto = tokenProvider.generateTokenResponse(user);
        user.changeJwtToken(tokenResponseDto.getRefreshToken());
        return tokenResponseDto;
    }

    /**
     * 닉네임 변경
     */
    @Transactional
    public void changeNickname(UserReq.ChangeNickname changeNickname) {
        if (isDuplicateNickname(changeNickname.getNickname())) throw new CustomConflictException(ErrorCode.ALREADY_SAVED_NICKNAME);
        Long userId = loginService.getLoginUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomNotFoundException(ErrorCode.NOT_FOUND_USER));
        user.changeNickname(changeNickname.getNickname());
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deleteUser() {
        Long userId = loginService.getLoginUserId();
        userRepository.deleteById(userId);
    }

    /**
     * 토큰 재발급
     */
    @Transactional
    public String getAccessToken(String refreshToken) {
        User user = userRepository.findByJwt(refreshToken)
                .orElseThrow(() -> new CustomNotFoundException(ErrorCode.NOT_FOUND_JWT));
        return tokenProvider.generateToken(user);
    }

    /**
     * 카카오 로그인
     */
    //Kakao
    @Value("${oauth.kakao.client-id}")
    private String CLIENT_ID;
    @Value("${oauth.kakao.redirect-uri}")
    private String REDIRECT_URI;
    @Value("${oauth.kakao.resource-uri}")
    private String RESOURCE_URI;
    @Value("${oauth.kakao.user-resource-uri}")
    private String USER_INFO;

    public TokenResponseDto socialLogin(String code) throws JsonProcessingException {

        String accessTokenResponse = getAccessTokenResponse(code);
        JsonNode userInfoResponse = getUserInfoByAccessTokenResponse(accessTokenResponse);
        return kakaoLogin(userInfoResponse);
    }

    private String getAccessTokenResponse(String code){
        // HttpHeader object 생성
        HttpHeaders headers = new HttpHeaders();;
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpHeader와 HttpBody를 하나의 obejct에 담기
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", CLIENT_ID);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> accessTokenRequest = new HttpEntity<>(params, headers);

        RestTemplate restTemplate = new RestTemplate();
        String responseBody = restTemplate.exchange(
                RESOURCE_URI,
                HttpMethod.POST,
                accessTokenRequest,
                String.class
        ).getBody();

        //액세스 토큰 파싱
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get("access_token").asText();
        } catch (Exception e){
            return e.toString();
        }
    }

    public JsonNode getUserInfoByAccessTokenResponse(String accessToken) throws JsonProcessingException {
        log.info("accesstoken = {}", accessToken);

        RestTemplate restTemplate = new RestTemplate();

        // HttpHeader object 생성
        HttpHeaders headers = new HttpHeaders();;
        headers.add("Authorization", "Bearer "+accessToken);

        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        HttpEntity<MultiValueMap<String, String>> userInfoRequest = new HttpEntity<>(headers);
        return restTemplate.exchange(USER_INFO, HttpMethod.POST, userInfoRequest, JsonNode.class).getBody(); // 유저 정보를 json으로 가져옴.

    }

    public TokenResponseDto kakaoLogin(JsonNode userResourceNode) throws JsonProcessingException {
        log.info("userResorceNode = {}", userResourceNode);

        String email = userResourceNode.get("kakao_account").get("email").asText();
        String is_email_verified = userResourceNode.get("kakao_account").get("is_email_verified").asText();

        // 카카오 서버 상에서 검증된 이메일인 경우
        if(is_email_verified.equals("true")){
            User user = null;
            Optional<User> userByKakao = userRepository.findUserByEmail(email);
            //로그인
            if(userByKakao.isPresent()){
                user = userByKakao.get();
            } else{
                //회원가입
                UserReq.SocialLogin newKakaoUserDto = UserReq.SocialLogin.builder()
                        .email(email)
                        .password(bCryptPasswordEncoder.encode(UUID.randomUUID().toString()))
                        .build();
                user = newKakaoUserDto.toEntity();
                userRepository.save(user);
            }
            TokenResponseDto tokenResponseDto = tokenProvider.generateTokenResponse(user);
            return tokenResponseDto;
        }
        throw new CustomNotFoundException(ErrorCode.NOT_FOUND_USER);
    }

}
