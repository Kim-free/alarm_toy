package com.example.toy_alarm.membership.dto.res;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor @Builder
@Getter @Setter
public class AfterCreateMembershipDto {
    private Long membershipId;
    private Long planId;

    public static AfterCreateMembershipDto toDto(Long membershipId, Long planId){
        return AfterCreateMembershipDto.builder()
                .membershipId(membershipId)
                .planId(planId)
                .build();
    }
}
