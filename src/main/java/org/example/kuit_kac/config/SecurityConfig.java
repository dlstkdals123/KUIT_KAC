package org.example.kuit_kac.config;

import org.example.kuit_kac.domain.oauth.service.KakaoOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final KakaoOAuth2UserService kakaoOAuth2UserService;

    public SecurityConfig(KakaoOAuth2UserService kakaoOAuth2UserService) {
        this.kakaoOAuth2UserService = kakaoOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                        .disable())
                .authorizeHttpRequests(authorize -> authorize
                        // 로그인 안 해도 접근 가능한 URL 전부 허용
                        .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
                        .anyRequest().permitAll()) // 기존 설정 유지 시
//                        .anyRequest().autenticated //  그 외 경로는 인증 필요 시
                // 카카오 로그인 설정 추가
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(kakaoOAuth2UserService) // 사용자 정보 조회 시 사용자 정의 서비스 사용
                        )
                        .defaultSuccessUrl("/loginSuccess", true)) // 로그인 성공 후 리다이렉트
                .logout(Customizer.withDefaults()) // 기본 로그아웃 설정
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }
}
