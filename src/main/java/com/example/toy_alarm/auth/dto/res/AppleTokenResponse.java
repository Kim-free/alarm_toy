package com.example.toy_alarm.auth.dto.res;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor @Builder
@Getter @Setter
public class AppleTokenResponse {
    private String accessToken;
    private String idToken;
    private String tokenType;
    private Integer expiresIn;
}
