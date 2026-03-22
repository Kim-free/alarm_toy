package com.example.toy_alarm.membership.dto.req;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor @Builder
@Getter @Setter
public class JoinByCodeDto {
    private String planCode;
    private String defaultMassage;
}
