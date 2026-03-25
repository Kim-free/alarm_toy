package com.example.toy_alarm.plan.dto.res;

import com.example.toy_alarm.plan.entity.Plan;
import lombok.*;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Builder
@Getter @Setter
public class PlanListDtos {
    private List<PlanListDto> planListDtos;

    public static PlanListDtos toDto(List<Plan> plans){
        List<PlanListDto> planList = plans.stream().map(PlanListDto::toDto).toList();

        return PlanListDtos.builder()
                .planListDtos(planList)
                .build();
    }
}
