package com.serengeti.hyu.backend.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthRequest {
    private String username;
    private String password;
    private String email;
}