package com.example.toy_alarm.plan.service;

import com.example.toy_alarm.appUser.repo.AppUserRepo;
import com.example.toy_alarm.auth.domain.LoginUser;
import com.example.toy_alarm.membership.entity.Membership;
import com.example.toy_alarm.membership.repo.MembershipRepo;
import com.example.toy_alarm.membership.service.MembershipService;
import com.example.toy_alarm.plan.dto.req.CreatePlanDto;
import com.example.toy_alarm.plan.dto.req.UpdatePlanDto;
import com.example.toy_alarm.plan.dto.res.*;
import com.example.toy_alarm.plan.entity.Plan;
import com.example.toy_alarm.plan.repo.PlanRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepo planRepo;
    private final MembershipService membershipService;
    private final MembershipRepo membershipRepo;
    private final AppUserRepo appUserRepo;

    public AfterCreatePlanDto create(CreatePlanDto createPlanDto, Long appUserId){
        Plan plan = planRepo.save(Plan.toEntity(createPlanDto, generateUniqueCode()));

        Membership membership = membershipService.createOwner(appUserId, plan.getId(), createPlanDto.getDefaultMessage());

        return AfterCreatePlanDto.toDto(plan.getId(), membership.getId());
    }

//    public PlanPolicyDto readPlan(Long membershipId){
//        Membership membership = membershipRepo.findById(membershipId).orElseThrow();
//        Plan plan = planRepo.findById(membershipId).orElseThrow();
//
//        return PlanPolicyDto.toDto(plan, membership.isLeader());
//    }

    @Transactional
    public void update(Long planId, UpdatePlanDto updatePlanDto) {
        Plan plan = planRepo.findById(planId).orElseThrow().update(updatePlanDto);
        planRepo.save(plan);
    }

    private String generateUniqueCode() {
        String code;

        do {
            code = Plan.generateCode();
        } while (planRepo.existsByPlanCode(code));

        return code;
    }

    public ResponseEditScreenDto readPolicy(Long membershipId) {
        Membership membership = membershipRepo.findById(membershipId).orElseThrow(() -> new RuntimeException("membership을 찾지 못햇습니다."));

        List<Membership> sortedMemberships = membershipService.sortMemberships(membership.getPlan(), membershipId);
        return ResponseEditScreenDto.toDto(membership.getPlan(), membership, sortedMemberships);
    }

    public PlanListDtos readPlans(LoginUser loginUser) {
        List<Long> planIds = membershipRepo.findAllByAppUserId(loginUser.getId()).stream()
                .map(membership -> membership.getPlan().getId()).toList();

        List<Plan> plans = planIds.stream().map(planId -> planRepo.findById(planId).orElseThrow(
                () -> new RuntimeException("membership을 찾지 못햇습니다."))).toList();


        return PlanListDtos.toDto(plans);
    }

    public MemberStatusDtos readMemberStatus(Long membershipId) {
        Membership membership = membershipRepo.findById(membershipId).orElseThrow(() -> new RuntimeException("membership을 찾지 못햇습니다."));

        List<Membership> sortedMemberships = membershipService.sortMemberships(membership.getPlan(), membershipId);
        return MemberStatusDtos.toDto(sortedMemberships);
    }
}
