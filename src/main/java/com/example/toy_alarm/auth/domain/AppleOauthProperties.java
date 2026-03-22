package com.example.toy_alarm.auth.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "oauth2")
public class AppleOauthProperties {
    private String clientId;

    private String teamId;

    private String keyId;

    private String privateKeyPath;

    private String redirectUri;

    private String authorizeUri;

    private String tokenUri;

    private String jwkSetUri;

    private List<String> scope;
}
