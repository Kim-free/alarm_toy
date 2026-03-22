package com.example.toy_alarm.plan.dto.req;

import lombok.*;

import java.time.DayOfWeek;
import java.util.Set;

@AllArgsConstructor @NoArgsConstructor @Builder
@Getter @Setter
public class CreatePlanDto {
    private String planTime;
    private String planName;
    private Set<DayOfWeek> repeatDays;
    private int maxSnooze;
    private String defaultMassage;
}
