package com.example.toy_alarm.auth.repo;

import com.example.toy_alarm.auth.domain.AppleOauthState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppleOauthStateRepo extends JpaRepository<AppleOauthState, Long> {
    Optional<AppleOauthState> findByStateAndProvider(String state, String provider);
}
