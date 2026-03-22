package com.example.toy_alarm.auth.dto.res;

import lombok.*;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@AllArgsConstructor @NoArgsConstructor @Builder
@Getter @Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AppleTokenResponse {
    private String accessToken;
    private String idToken;
    private String tokenType;
    private Integer expiresIn;
}
