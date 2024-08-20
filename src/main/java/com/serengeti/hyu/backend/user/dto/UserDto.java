package com.serengeti.hyu.backend.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDto {
    private String name;
    private Date birth;
    private String username;
    private String password;
    private String phoneNumber;
    private String email;
}