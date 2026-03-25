package com.example.toy_alarm.auth.controller;

import com.example.toy_alarm.auth.dto.res.LoginResponseDto;
import com.example.toy_alarm.auth.infrastructure.AppleOauthClient;
import com.example.toy_alarm.auth.service.AppleOauthService;
import com.example.toy_alarm.auth.service.AppleOauthStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/apple")
@ConditionalOnProperty(name = "apple.enabled", havingValue = "true")
public class AppleOauthController {
    private final AppleOauthService appleOauthService;
    private final AppleOauthStateService appleOauthStateService;
    private final AppleOauthClient appleOauthClient;

    @GetMapping("/login")
    public ResponseEntity<?> redirectToApple() {

        String state = appleOauthStateService.createState();
        String authorizeUrl = appleOauthClient.buildAuthorizeUrl(state);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(authorizeUrl))
                .build();
    }

    @PostMapping("/callback")
    public ResponseEntity<LoginResponseDto> callback(
            @RequestParam("code") String code,
            @RequestParam("state") String state
    ) {
        appleOauthStateService.validateState(state);
        LoginResponseDto response = appleOauthService.login(code);

        String accessToken = response.getAccessToken();

        // 3. 쿠키 생성
        ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)        // JS 접근 불가 (보안)
                .secure(false)         // HTTPS면 true (배포 시)
                .path("/")             // 모든 경로에서 사용
                .maxAge(60 * 60)       // 1시간
                .sameSite("Lax")       // CSRF 완화
                .build();

        // 4. 프론트로 리다이렉트
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .location(URI.create("http:front.com"))
                .build();
    }
}
