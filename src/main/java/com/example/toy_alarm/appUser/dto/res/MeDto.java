package com.example.toy_alarm.appUser.dto.res;

import com.example.toy_alarm.appUser.entity.AppUser;
import com.example.toy_alarm.auth.domain.LoginUser;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor @Builder
@Getter @Setter
public class MeDto {
    private Long userId;
    private String nickname;
    private boolean profileCompleted;

    public static MeDto toDto(AppUser user) {
        boolean profileCompleted = user.getNickname() != null && !user.getNickname().isBlank();

        return MeDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profileCompleted(profileCompleted)
                .build();
    }
}
