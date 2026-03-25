package com.example.toy_alarm.plan.entity;

import com.example.toy_alarm.plan.dto.req.CreatePlanDto;
import com.example.toy_alarm.plan.dto.req.UpdatePlanDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@AllArgsConstructor @NoArgsConstructor @Builder
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String planCode;

    private String planName;
    private String planTime;
    private int maxSnooze;

    @ElementCollection
    @CollectionTable(
            name = "repeat_days",
            joinColumns = @JoinColumn(name = "plan_id")
    )
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<DayOfWeek> repeatDays = new HashSet<>();

    public static Plan toEntity(CreatePlanDto createPlanDto, String planCode){
        return Plan.builder()
                .planCode(planCode)
                .planName(createPlanDto.getPlanName())
                .planTime(createPlanDto.getPlanTime())
                .maxSnooze(createPlanDto.getMaxSnooze())
                .repeatDays(createPlanDto.getRepeatDays())
                .build();
    }

    public Plan update(UpdatePlanDto updatePlanDto){
        this.planName = updatePlanDto.getPlanName();
        this.planTime = updatePlanDto.getPlanTime();
        this.maxSnooze = updatePlanDto.getMaxSnooze();
        this.repeatDays = updatePlanDto.getRepeatDays();

        return this;
    }

    public static String generateCode(){
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int LENGTH = 6;
        SecureRandom random = new SecureRandom();

        StringBuilder code = new StringBuilder();

        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }

        return code.toString();
    }

}
