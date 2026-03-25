package com.example.toy_alarm.auth.service;

import com.example.toy_alarm.appUser.entity.AppUser;
import com.example.toy_alarm.appUser.service.AppUserService;
import com.example.toy_alarm.auth.infrastructure.JwtProvider;
import com.example.toy_alarm.auth.dto.res.AppleTokenResponse;
import com.example.toy_alarm.auth.dto.res.AppleUserInfo;
import com.example.toy_alarm.auth.dto.res.LoginResponseDto;
import com.example.toy_alarm.auth.infrastructure.AppleOauthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "apple.enabled", havingValue = "true")
public class AppleOauthService {
    private final AppleOauthClient appleOauthClient;
    private final AppUserService appUserService;
    private final JwtProvider jwtProvider;

    public LoginResponseDto login(String code){
        // 1. token 요청
        AppleTokenResponse tokenResponse = appleOauthClient.requestToken(code);

        // 2. id_token 파싱
        AppleUserInfo userInfo = appleOauthClient.parseIdToken(tokenResponse.getIdToken());

        // 3. id_token 검증
        appleOauthClient.validateIdToken(tokenResponse.getIdToken());

        // 4. 사용자 조회 or 생성
        AppUser user = appUserService.findOrCreateUser(userInfo);

        // 5. JWT 발급
        String accessToken = jwtProvider.createAccessToken(user.getId());

        boolean profileCompleted = user.getNickname() != null && !user.getNickname().isBlank();
        // 6. 응답
        return LoginResponseDto.toDto(user.getId(), accessToken, profileCompleted);
    }
}
