package com.example.toy_alarm.auth.repo;

import com.example.toy_alarm.auth.domain.LoginUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginUserRepo extends JpaRepository<LoginUser, Long> {
}
