package com.example.toy_alarm.plan.controller;

import com.example.toy_alarm.auth.domain.Login;
import com.example.toy_alarm.auth.domain.LoginUser;
import com.example.toy_alarm.plan.dto.req.CreatePlanDto;
import com.example.toy_alarm.plan.dto.req.UpdatePlanDto;
import com.example.toy_alarm.plan.dto.res.*;
import com.example.toy_alarm.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plan")
public class PlanController {
    private final PlanService planService;

    @PostMapping
    public ResponseEntity<AfterCreatePlanDto> create(@Login LoginUser loginUser, @RequestBody CreatePlanDto createPlanDto){
        AfterCreatePlanDto response = planService.create(createPlanDto, loginUser.getId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{planId}/policy")
    public void update(@PathVariable Long planId, @RequestBody UpdatePlanDto updatePlanDto){
        planService.update(planId, updatePlanDto);
    }

    @GetMapping("/{membershipId}/policy")
    public ResponseEntity<ResponseEditScreenDto> readPolicy(@PathVariable Long membershipId){
        ResponseEditScreenDto response = planService.readPolicy(membershipId);
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/{membershipId}") // 그냥 조회 화면??
//    public ResponseEntity<PlanPolicyDto> readPlan(@PathVariable Long membershipId){
//        PlanPolicyDto response = planService.readPlan(membershipId);
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/list")
    public ResponseEntity<PlansDto> readPlans(@Login LoginUser loginUser){
        PlansDto response = planService.readPlans(loginUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{membershipId}/memberStatus")
    public ResponseEntity<MemberStatusDtos> readMemberStatus(@PathVariable Long membershipId){
        MemberStatusDtos response = planService.readMemberStatus(membershipId);

        return ResponseEntity.ok(response);
    }
}
