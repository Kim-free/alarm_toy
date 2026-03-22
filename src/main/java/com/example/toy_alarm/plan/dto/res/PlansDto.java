package com.example.toy_alarm.plan.dto.res;

import lombok.*;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Builder
@Getter @Setter
public class PlansDto {
    private List<Long> planIds;

    public static PlansDto toDto(List<Long> planIds){
        return PlansDto.builder()
                .planIds(planIds)
                .build();
    }
}
