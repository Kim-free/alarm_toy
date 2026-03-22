package com.example.toy_alarm.plan.dto.res;

import com.example.toy_alarm.appUser.entity.AppUser;
import com.example.toy_alarm.membership.entity.Membership;
import com.example.toy_alarm.plan.entity.Plan;
import lombok.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

@AllArgsConstructor @NoArgsConstructor @Builder
@Getter @Setter
public class ResponseEditScreenDto {
    private String groupCode;
    private List<MembersNameDto> membersName;
    private String planTime;
    private Set<DayOfWeek> repeatDays;
    private int maxSnooze;
    private boolean isLeader;

    public static ResponseEditScreenDto toDto(Plan plan, Membership membership, List<Membership> sortedMemberships){
        List<MembersNameDto> membersNameDtos = sortedMemberships.stream().map(
                member -> MembersNameDto.builder()
                        .name(member.getAppUser().getNickname())
                        .isLeader(member.isLeader())
                        .build()
        ).toList();

        return ResponseEditScreenDto.builder()
                .groupCode(plan.getPlanCode())
                .membersName(membersNameDtos)
                .planTime(plan.getPlanTime())
                .repeatDays(plan.getRepeatDays())
                .maxSnooze(plan.getMaxSnooze())
                .isLeader(membership.isLeader())
                .build();
    }
}
