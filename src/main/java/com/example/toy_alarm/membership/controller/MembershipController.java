package com.example.toy_alarm.membership.controller;

import com.example.toy_alarm.auth.domain.Login;
import com.example.toy_alarm.auth.domain.LoginUser;
import com.example.toy_alarm.membership.dto.req.JoinByCodeDto;
import com.example.toy_alarm.membership.dto.res.AfterCreateMembershipDto;
import com.example.toy_alarm.membership.entity.Status;
import com.example.toy_alarm.membership.service.MembershipService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/membership")
public class MembershipController {
    private final MembershipService membershipService;

    @PostMapping()
    public AfterCreateMembershipDto create(@Parameter(hidden = true) @Login LoginUser loginUser, @RequestBody JoinByCodeDto joinByCodeDto){
        return membershipService.joinByCode(loginUser.getId(), joinByCodeDto);
    }

    @GetMapping("/{membershipId}/role")
    public boolean isLeader(@PathVariable Long membershipId){
        return membershipService.isLeader(membershipId);
    }

    @PatchMapping("/{membershipId}")
    public void update(@PathVariable Long membershipId, @RequestParam Status status){
        membershipService.update(membershipId, status);
    }
}
