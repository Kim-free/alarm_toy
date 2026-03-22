package com.example.toy_alarm.appUser.service;

import com.example.toy_alarm.appUser.dto.res.MeDto;
import com.example.toy_alarm.appUser.entity.AppUser;
import com.example.toy_alarm.appUser.repo.AppUserRepo;
import com.example.toy_alarm.auth.domain.LoginUser;
import com.example.toy_alarm.auth.dto.res.AppleUserInfo;
import com.example.toy_alarm.membership.repo.MembershipRepo;
import com.example.toy_alarm.plan.repo.PlanRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepo appUserRepo;
    private final PlanRepo planRepo;
    private final MembershipRepo membershipRepo;

//    public HomeScreenDto homeScreen(Long appUserId){
//        //
//
//        List<Membership> memberships = membershipRepo.findAllByAppUserId(appUserId);
//
//        memberships.stream().map(
//                membership -> )
//    }

    @Transactional
    public AppUser findOrCreateUser(AppleUserInfo userInfo) {
        return appUserRepo.findByAppleSub(userInfo.getSub())
                .map(existingUser -> {
                    updateEmailIfNeeded(existingUser, userInfo.getEmail());
                    return existingUser;
                })
                .orElseGet(() -> createUser(userInfo));
    }

    private AppUser createUser(AppleUserInfo userInfo) {
        AppUser newUser = AppUser.builder()
                .appleSub(userInfo.getSub())
                .email(userInfo.getEmail())
                .build();

        return appUserRepo.save(newUser);
    }

    private void updateEmailIfNeeded(AppUser user, String email) {
        if (email == null || email.isBlank()) {
            return;
        }

        if (user.getEmail() == null || !user.getEmail().equals(email)) {
            user.updateEmail(email);
        }
    }

    @Transactional
    public void update(LoginUser loginUser, String nickname) {
        appUserRepo.findById(loginUser.getId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."))
                .updateNickname(nickname);
    }

    public MeDto me(LoginUser loginUser) {
        AppUser appUser = appUserRepo.findById(loginUser.getId()).orElseThrow(
                () -> new RuntimeException("유저를 찾을 수 없습니다."));
        return MeDto.toDto(appUser);
    }
}
