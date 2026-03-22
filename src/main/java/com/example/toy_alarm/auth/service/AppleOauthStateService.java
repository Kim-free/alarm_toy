package com.example.toy_alarm.auth.service;

import com.example.toy_alarm.auth.domain.AppleOauthState;
import com.example.toy_alarm.auth.repo.AppleOauthStateRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppleOauthStateService {

    private static final String PROVIDER = "apple";
    private static final long EXPIRE_MINUTES = 5L;

    private final AppleOauthStateRepo appleOauthStateRepository;

    @Transactional
    public String createState() {
        String state = UUID.randomUUID().toString();

        AppleOauthState appleOauthState = AppleOauthState.builder()
                .state(state)
                .provider(PROVIDER)
                .expiresAt(LocalDateTime.now().plusMinutes(EXPIRE_MINUTES))
                .isUsed(false)
                .createdAt(LocalDateTime.now())
                .build();

        appleOauthStateRepository.save(appleOauthState);
        return state;
    }

    @Transactional
    public void validateState(String state) {
        AppleOauthState appleOauthState = appleOauthStateRepository
                .findByStateAndProvider(state, "apple")
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 state 입니다."));

        if (appleOauthState.isExpired()) {
            appleOauthStateRepository.delete(appleOauthState);
            throw new IllegalArgumentException("만료된 state 입니다.");
        }

        appleOauthStateRepository.delete(appleOauthState);
    }
}