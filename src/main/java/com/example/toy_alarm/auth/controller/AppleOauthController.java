package com.example.toy_alarm.auth.controller;

import com.example.toy_alarm.auth.dto.res.LoginResponseDto;
import com.example.toy_alarm.auth.infrastructure.AppleOauthClient;
import com.example.toy_alarm.auth.service.AppleOauthService;
import com.example.toy_alarm.auth.service.AppleOauthStateService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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

    @Hidden
    @PostMapping("/callback")
    public ResponseEntity<LoginResponseDto> callback(
            @RequestParam("code") String code,
            @RequestParam("state") String state
    ) {
        appleOauthStateService.validateState(state);
        LoginResponseDto response = appleOauthService.login(code);

        String accessToken = response.getAccessToken();
        String encodedToken = URLEncoder.encode(accessToken, StandardCharsets.UTF_8);

        // 4. 프론트로 리다이렉트
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create("ilarm://login/callback?token=" + encodedToken))
                .build();
    }
}
