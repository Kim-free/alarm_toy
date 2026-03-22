package com.example.toy_alarm.membership.entity;

import lombok.Getter;

@Getter
public enum Status {
    READY("준비"),
    ATTENDANCE("참여"),
    ABSENCE("불참");

    private final String statusMessage;

    Status(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
