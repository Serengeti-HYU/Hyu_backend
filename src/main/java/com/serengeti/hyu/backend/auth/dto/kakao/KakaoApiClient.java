package com.serengeti.hyu.backend.auth.dto.kakao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class KakaoApiClient {

    private static final Logger log = LoggerFactory.getLogger(KakaoApiClient.class);

    @Value("${kakao.client-id:default-client-id}")
    private String clientId;

    @Value("${kakao.client-secret:default-client-secret}")
    private String clientSecret;

    @Value("${kakao.redirect-uri:http://localhost:8080/oauth2/kakao}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    public KakaoInfoResponse getUserInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<KakaoInfoResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, KakaoInfoResponse.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("Failed to retrieve Kakao user info. Response: {}", e.getResponseBodyAsString());
            throw e;
        }
    }

    public KakaoTokens getAccessToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        body.add("client_secret", clientSecret); // 추가된 client_secret 파라미터

        HttpEntity<?> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<KakaoTokens> response = restTemplate.exchange(url, HttpMethod.POST, entity, KakaoTokens.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("Failed to retrieve Kakao access token. Response: {}", e.getResponseBodyAsString());
            throw e;
        }
    }
}
