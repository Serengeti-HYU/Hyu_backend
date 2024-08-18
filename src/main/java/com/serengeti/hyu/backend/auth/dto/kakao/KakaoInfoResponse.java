package com.serengeti.hyu.backend.auth.dto.kakao;

import lombok.Data;

import java.util.Map;

@Data
public class KakaoInfoResponse {
    private String id;
    private Map<String, Object> properties;
    private KakaoAccount kakaoAccount;

    public static KakaoInfoResponse from(Map<String, Object> attributes) {
        KakaoInfoResponse response = new KakaoInfoResponse();
        response.setId(String.valueOf(attributes.get("id")));
        response.setProperties((Map<String, Object>) attributes.get("properties"));
        response.setKakaoAccount(KakaoAccount.from((Map<String, Object>) attributes.get("kakao_account")));
        return response;
    }

    @Data
    public static class KakaoAccount {
        private String email;

        public static KakaoAccount from(Map<String, Object> attributes) {
            KakaoAccount account = new KakaoAccount();
            account.setEmail((String) attributes.get("email"));
            return account;
        }
    }
}
