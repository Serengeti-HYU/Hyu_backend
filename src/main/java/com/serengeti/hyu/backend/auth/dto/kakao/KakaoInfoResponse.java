package com.serengeti.hyu.backend.auth.dto.kakao;


import lombok.Data;

@Data
public class KakaoInfoResponse {

    private String id;
    private Properties properties;
    private KakaoAccount kakaoAccount;

    @Data
    public static class Properties {
        private String nickname;
    }

    @Data
    public static class KakaoAccount {
        private String email;
    }
}
