package com.example.toy_alarm.auth.dto.res;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor @Builder
@Getter @Setter
public class LoginResponseDto {
    private Long userId;
    private String accessToken;
    private boolean profileCompleted;

    public static LoginResponseDto toDto(Long userId, String accessToken, boolean profileCompleted){
        return LoginResponseDto.builder()
                .userId(userId)
                .accessToken(accessToken)
                .profileCompleted(profileCompleted)
                .build();
    }
}
