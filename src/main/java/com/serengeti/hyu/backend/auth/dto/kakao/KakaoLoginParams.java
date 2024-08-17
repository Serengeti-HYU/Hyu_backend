package com.serengeti.hyu.backend.auth.dto.kakao;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoLoginParams {
    private String clientId;
    private String redirectUri;
    private String code;
}
