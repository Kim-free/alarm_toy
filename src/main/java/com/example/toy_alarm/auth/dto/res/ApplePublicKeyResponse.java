package com.example.toy_alarm.auth.dto.res;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ApplePublicKeyResponse {
    private List<ApplePublicKey> keys;
}