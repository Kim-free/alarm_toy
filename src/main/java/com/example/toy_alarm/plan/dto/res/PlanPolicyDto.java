package com.example.toy_alarm.plan.dto.res;

import com.example.toy_alarm.plan.entity.Plan;
import lombok.*;

import java.time.DayOfWeek;
import java.util.Set;

@AllArgsConstructor @NoArgsConstructor @Builder
@Getter @Setter
public class PlanPolicyDto {
    private String planTime;
    private String planName;
    private Set<DayOfWeek> repeatDays;
    private int maxSnooze;
    private boolean isLeader;

    public static PlanPolicyDto toDto(Plan plan, boolean isLeader){
        return PlanPolicyDto.builder()
                .planTime(plan.getPlanTime())
                .planName(plan.getPlanName())
                .maxSnooze(plan.getMaxSnooze())
                .isLeader(isLeader)
                .build();
    }
}
