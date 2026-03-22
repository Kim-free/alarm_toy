package com.example.toy_alarm.plan.dto.res;

import com.example.toy_alarm.membership.entity.Membership;
import com.example.toy_alarm.membership.entity.Status;
import lombok.*;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Builder
@Getter @Setter
public class MemberStatusDtos {
    private List<MemberStatusDto> memberStatusDtos;

    public static MemberStatusDtos toDto(List<Membership> memberships){

        return MemberStatusDtos.builder()
                .memberStatusDtos(memberships.stream()
                        .map(MemberStatusDto::toDto)
                        .toList())
                .build();
    }

    @AllArgsConstructor @NoArgsConstructor @Builder
    @Getter @Setter
    public static class MemberStatusDto{
        private String memberName;
        private boolean isLeader;
        private Status memberStatus;
        private String displayMessage;

        public static MemberStatusDto toDto(Membership membership){
            return MemberStatusDto.builder()
                    .memberName(membership.getAppUser().getNickname())
                    .isLeader(membership.isLeader())
                    .memberStatus(membership.getStatus())
                    .displayMessage(membership.getDefaultMassage())
                    .build();
        }
    }
}
