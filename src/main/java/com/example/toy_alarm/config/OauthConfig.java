package com.example.toy_alarm.config;

import com.example.toy_alarm.auth.domain.AppleOauthProperties;
import com.example.toy_alarm.auth.infrastructure.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(AppleOauthProperties.class)
@RequiredArgsConstructor
public class OauthConfig {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://appleid.apple.com")
                .build();
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter() {
        FilterRegistrationBean<JwtAuthenticationFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(jwtFilter);
        bean.addUrlPatterns("/*"); // 전체 적용
        return bean;
    }
}
