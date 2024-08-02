package com.serengeti.hyu.backend.user.dto;


import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter @Setter
public class SignUpDto {

    private String name; //유저이름
    private Date birth;

    private String password;

    private String username; //로그인 아이디
    private String phoneNumber;
    private String email;


}
