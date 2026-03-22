package com.example.toy_alarm.appUser.repo;

import com.example.toy_alarm.appUser.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppUserRepo extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByAppleSub(String appleSub);
}
