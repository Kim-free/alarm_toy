package com.example.toy_alarm.membership.entity;

import com.example.toy_alarm.appUser.entity.AppUser;
import com.example.toy_alarm.plan.entity.Plan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor @NoArgsConstructor @Builder
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appUser_id")
    private AppUser appUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @Enumerated(EnumType.STRING)
    private Status status;
    private String defaultMassage;

    private boolean isLeader;

    public void updateStatus(Status status){
        this.status = status;
    }

    public static Membership toEntity(AppUser appUser, Plan plan, boolean isLeader, String defaultMassage){
        return Membership.builder()
                .appUser(appUser)
                .plan(plan)
                .status(Status.ABSENCE)
                .isLeader(isLeader)
                .defaultMassage(defaultMassage)
                .build();
    }
}
