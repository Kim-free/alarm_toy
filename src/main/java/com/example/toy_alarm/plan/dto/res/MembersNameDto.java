package com.example.toy_alarm.plan.dto.res;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor @Builder
@Getter @Setter
public class MembersNameDto {
    private String name;
    private boolean isLeader;
}
