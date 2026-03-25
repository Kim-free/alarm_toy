package com.example.toy_alarm.appUser.controller;

import com.example.toy_alarm.appUser.dto.req.UpdateNicknameDto;
import com.example.toy_alarm.appUser.dto.res.MeDto;
import com.example.toy_alarm.appUser.service.AppUserService;
import com.example.toy_alarm.auth.domain.Login;
import com.example.toy_alarm.auth.domain.LoginUser;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appUser")
public class AppUserController {
    private final AppUserService appUserService;

//    @GetMapping("/home")
//    public HomeScreenDto homeScreen(@Login LoginUser loginUser){
//        return appUserService.homeScreen(loginUser.getId());
//    }

    @PatchMapping("/nickname")
    public void update(@Parameter(hidden = true) @Login LoginUser loginUser, @RequestBody UpdateNicknameDto updateNicknameDto){
        appUserService.update(loginUser, updateNicknameDto.getNickname());
    }

    @GetMapping("/me")
    public ResponseEntity<MeDto> me(@Parameter(hidden = true) @Login LoginUser loginUser){
        MeDto response = appUserService.me(loginUser);
        return ResponseEntity.ok(response);
    }
}
