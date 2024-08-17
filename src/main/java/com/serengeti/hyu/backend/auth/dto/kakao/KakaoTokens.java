package com.serengeti.hyu.backend.auth.dto.kakao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoTokens {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private int expires_in;
    private int refresh_token_expires_in;
}
