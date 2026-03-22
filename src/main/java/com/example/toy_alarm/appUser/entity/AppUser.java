package com.example.toy_alarm.appUser.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor @NoArgsConstructor @Builder
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String nickname;

    @Column(nullable = false, unique = true)
    private String appleSub;

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }
}
