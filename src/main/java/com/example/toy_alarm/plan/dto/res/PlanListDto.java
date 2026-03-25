package com.example.toy_alarm.plan.dto.res;

import com.example.toy_alarm.plan.entity.Plan;
import lombok.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

@AllArgsConstructor @NoArgsConstructor @Builder
@Getter @Setter
public class PlanListDto {
    private Long planId;
    private String planName;
    private Set<DayOfWeek> repeatDays;

    public static PlanListDto toDto(Plan plan) {
        return PlanListDto.builder()
                .planId(plan.getId())
                .planName(plan.getPlanName())
                .repeatDays(plan.getRepeatDays())
                .build();
    }
}
