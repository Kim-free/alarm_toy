package com.example.toy_alarm.plan.dto.res;

import com.example.toy_alarm.plan.entity.Plan;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor @Builder
@Getter @Setter
public class AfterCreatePlanDto {
    private Long membershipId;
    private Long planId;

    public static AfterCreatePlanDto toDto(Long planId, Long membershipId){
        return AfterCreatePlanDto.builder()
                .planId(planId)
                .membershipId(membershipId)
                .build();
    }
}
