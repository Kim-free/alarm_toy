package com.example.toy_alarm.auth.infrastructure;

import com.example.toy_alarm.auth.domain.AppleOauthProperties;
import com.example.toy_alarm.auth.dto.res.ApplePublicKey;
import com.example.toy_alarm.auth.dto.res.ApplePublicKeyResponse;
import com.example.toy_alarm.auth.dto.res.AppleTokenResponse;
import com.example.toy_alarm.auth.dto.res.AppleUserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class AppleOauthClient {
    private final WebClient webClient;
    private final AppleOauthProperties properties;
    private PrivateKey privateKey;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        this.privateKey = getPrivateKey(properties.getPrivateKeyPath());
    }

    public String buildAuthorizeUrl(String state) {
        return properties.getAuthorizeUri()
                + "?client_id=" + properties.getClientId()
                + "&redirect_uri=" + properties.getRedirectUri()
                + "&response_type=code"
                + "&response_mode=form_post"
                + "&scope=" + URLEncoder.encode(String.join(" ", properties.getScope()), StandardCharsets.UTF_8)
                + "&state=" + state;
    }

    public AppleTokenResponse requestToken(String code) {

        String clientSecret = generateClientSecret();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", properties.getClientId());
        body.add("client_secret", clientSecret);
        body.add("code", code);
        body.add("grant_type", "authorization_code");
        body.add("redirect_uri", properties.getRedirectUri());

        return webClient.post()
                .uri("/auth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(body)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .map(errorBody -> new RuntimeException("Apple token 요청 실패: " + errorBody))
                )
                .bodyToMono(AppleTokenResponse.class)
                .block();
    }

    public String generateClientSecret() {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 1000 * 60 * 5);

        return Jwts.builder()
                .setHeaderParam("kid", properties.getKeyId())
                .setIssuer(properties.getTeamId())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setAudience("https://appleid.apple.com")
                .setSubject(properties.getClientId())
                .signWith(privateKey, SignatureAlgorithm.ES256)
                .compact();
    }

    private PrivateKey getPrivateKey(String privateKeyPem) {
        try {
            String normalizedKey = privateKeyPem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(normalizedKey);

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");

            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new IllegalArgumentException("Apple private key 파싱에 실패했습니다.", e);
        }
    }



    public AppleUserInfo parseIdToken(String idToken) {
        try {
            String[] parts = idToken.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("유효하지 않은 id_token 형식입니다.");
            }

            // payload 추출
            String payload = parts[1];

            // Base64URL 디코딩
            byte[] decodedBytes = Base64.getUrlDecoder().decode(payload);
            String payloadJson = new String(decodedBytes, StandardCharsets.UTF_8);

            // JSON 파싱
            JsonNode jsonNode = objectMapper.readTree(payloadJson);

            String sub = getText(jsonNode, "sub");
            String email = getText(jsonNode, "email");
            Boolean emailVerified = getBoolean(jsonNode, "email_verified");

            if (sub == null || sub.isBlank()) {
                throw new IllegalArgumentException("id_token에 sub가 없습니다.");
            }

            return AppleUserInfo.builder()
                    .sub(sub)
                    .email(email)
                    .emailVerified(Boolean.TRUE.equals(emailVerified))
                    .build();

        } catch (Exception e) {
            throw new IllegalArgumentException("id_token 파싱 실패", e);
        }
    }

    private String getText(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        return (field != null && !field.isNull()) ? field.asText() : null;
    }

    private Boolean getBoolean(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        if (field == null || field.isNull()) return null;

        if (field.isBoolean()) {
            return field.asBoolean();
        }

        String value = field.asText();
        if ("true".equalsIgnoreCase(value)) return true;
        if ("false".equalsIgnoreCase(value)) return false;

        return null;
    }

    public void validateIdToken(String idToken) {
        try {
            String[] parts = idToken.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("유효하지 않은 id_token 형식입니다.");
            }

            // 1) header 읽기
            String headerJson = new String(
                    Base64.getUrlDecoder().decode(parts[0]),
                    StandardCharsets.UTF_8
            );
            JsonNode header = objectMapper.readTree(headerJson);

            String kid = getRequiredText(header, "kid");
            String alg = getRequiredText(header, "alg");

            // Apple Sign in with Apple의 ID 토큰은 RS256인 경우가 일반적
            if (!"RS256".equals(alg)) {
                throw new IllegalArgumentException("지원하지 않는 id_token alg 입니다: " + alg);
            }

            // 2) Apple 공개키 목록 가져오기
            ApplePublicKeyResponse keyResponse = webClient.get()
                    .uri("https://appleid.apple.com/auth/keys")
                    .retrieve()
                    .bodyToMono(ApplePublicKeyResponse.class)
                    .block();

            if (keyResponse == null || keyResponse.getKeys() == null || keyResponse.getKeys().isEmpty()) {
                throw new IllegalStateException("Apple 공개키를 가져오지 못했습니다.");
            }

            ApplePublicKey matchedKey = keyResponse.getKeys().stream()
                    .filter(k -> Objects.equals(k.getKid(), kid) && Objects.equals(k.getAlg(), alg))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("일치하는 Apple 공개키가 없습니다. kid=" + kid));

            PublicKey publicKey = buildRsaPublicKey(matchedKey);

            // 3) 서명 + exp 파싱/검증
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(idToken)
                    .getBody();

            // 4) claim 검증
            String iss = claims.getIssuer();
            String aud = claims.getAudience();

            if (!"https://appleid.apple.com".equals(iss)) {
                throw new IllegalArgumentException("유효하지 않은 iss 입니다.");
            }

            if (!properties.getClientId().equals(aud)) {
                throw new IllegalArgumentException("유효하지 않은 aud 입니다.");
            }

        } catch (JwtException e) {
            throw new IllegalArgumentException("id_token 서명 또는 만료 검증 실패", e);
        } catch (Exception e) {
            throw new IllegalArgumentException("id_token 검증 실패", e);
        }
    }

    private String getRequiredText(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        if (field == null || field.isNull() || field.asText().isBlank()) {
            throw new IllegalArgumentException("JWT header에 " + fieldName + "가 없습니다.");
        }
        return field.asText();
    }

    private PublicKey buildRsaPublicKey(ApplePublicKey key) {
        try {
            byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
            byte[] eBytes = Base64.getUrlDecoder().decode(key.getE());

            BigInteger n = new BigInteger(1, nBytes);
            BigInteger e = new BigInteger(1, eBytes);

            java.security.spec.RSAPublicKeySpec keySpec =
                    new java.security.spec.RSAPublicKeySpec(n, e);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new IllegalArgumentException("Apple RSA 공개키 생성 실패", e);
        }
    }
}
