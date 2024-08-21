package com.serengeti.hyu.backend.config;

import com.serengeti.hyu.backend.auth.service.CustomOauth2UserService;
import com.serengeti.hyu.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {


    private  UserRepository userRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomOauth2UserService customOauth2UserService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, CustomOauth2UserService customOauth2UserService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customOauth2UserService = customOauth2UserService;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/user/signup","/oauth2/**", "/user/login").permitAll()
                                .requestMatchers("/**").permitAll() // 그 외 인증 없이 접근X
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2 //소셜로그인
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOauth2UserService)
                        )
                        .defaultSuccessUrl("/user/loginSuccess", true)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutSuccessUrl("/")
                );

        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        return new DefaultOAuth2UserService();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
