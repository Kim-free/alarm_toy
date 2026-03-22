package com.example.toy_alarm.membership.service;

import com.example.toy_alarm.appUser.entity.AppUser;
import com.example.toy_alarm.appUser.repo.AppUserRepo;
import com.example.toy_alarm.membership.dto.req.JoinByCodeDto;
import com.example.toy_alarm.membership.dto.res.AfterCreateMembershipDto;
import com.example.toy_alarm.membership.entity.Membership;
import com.example.toy_alarm.membership.entity.Status;
import com.example.toy_alarm.membership.repo.MembershipRepo;
import com.example.toy_alarm.plan.entity.Plan;
import com.example.toy_alarm.plan.repo.PlanRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MembershipService {
    private final MembershipRepo membershipRepo;
    private final PlanRepo planRepo;
    private final AppUserRepo appUserRepo;

    public Membership createOwner(Long appUserId, Long planId, String defaultMassage) {
        Plan plan = planRepo.findById(planId).orElseThrow(()-> new RuntimeException("AppUser을 찾을 수 없습니다."));

        AppUser appUser = appUserRepo.findById(appUserId).orElseThrow(
                ()-> new RuntimeException("AppUser을 찾을 수 없습니다."));

        return membershipRepo.save(Membership.toEntity(appUser, plan, true, defaultMassage));
    }

    public AfterCreateMembershipDto joinByCode(Long appUserId, JoinByCodeDto joinByCodeDto){ // 초대코드로 create
        AppUser appUser = appUserRepo.findById(appUserId).orElseThrow(
                ()-> new RuntimeException("AppUser을 찾을 수 없습니다."));

        Plan plan = planRepo.findByPlanCode(joinByCodeDto.getPlanCode()).orElseThrow(
                () -> new RuntimeException("그룹을 찾을 수 없습니다."));
        Membership membership = membershipRepo.save(Membership.toEntity(appUser, plan, false, joinByCodeDto.getDefaultMassage()));

        return AfterCreateMembershipDto.toDto(membership.getId(), plan.getId());
    }

    public boolean isLeader(Long membershipId){
        Membership membership = membershipRepo.findById(membershipId).orElseThrow();
        return membership.isLeader();
    }

    @Transactional
    public void update(Long membershipId, Status status) {
        Membership membership = membershipRepo.findById(membershipId).orElseThrow(
                ()-> new RuntimeException("membership을 찾을 수 없습니다."));

        membership.updateStatus(status);
    }

    public List<Membership> sortMemberships(Plan plan, Long membershipId){
        List<Membership> memberships = membershipRepo.findAllByPlanId(plan.getId());

        return memberships.stream()
                .sorted(Comparator
                        .comparing((Membership m) -> !m.getId().equals(membershipId))
                        .thenComparing(m -> m.getAppUser().getNickname())
                )
                .toList();
    }
}
