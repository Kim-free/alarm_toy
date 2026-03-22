package com.example.toy_alarm.auth.dto.res;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor @Builder
@Getter @Setter
public class AppleUserInfo {
    private String sub;
    private String email;
    private boolean emailVerified;
}
