package org.example.kuit_kac.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.kuit_kac.domain.oauth.service.KakaoOAuth2UserService;
import org.example.kuit_kac.domain.user.service.UserService;
import org.example.kuit_kac.exception.CustomAccessDenialHandler;
import org.example.kuit_kac.exception.CustomAuthenticationEntryPoint;
import org.example.kuit_kac.exception.CustomException;
import org.example.kuit_kac.exception.ErrorCode;
import org.example.kuit_kac.global.filter.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final KakaoOAuth2UserService kakaoOAuth2UserService;
    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDenialHandler denialHandler;

    public SecurityConfig(KakaoOAuth2UserService kakaoOAuth2UserService, JwtAuthFilter jwtAuthFilter, OAuth2SuccessHandler oAuth2SuccessHandler, CustomAuthenticationEntryPoint authenticationEntryPoint, CustomAccessDenialHandler denialHandler) {
        this.kakaoOAuth2UserService = kakaoOAuth2UserService;
        this.jwtAuthFilter = jwtAuthFilter;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.denialHandler = denialHandler;
    }

    @Bean
    @Profile("dev")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // H2 콘솔은 CRSF 예외, 전반적으로는 비활성화
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                        .disable())
                // JWT 쓰므로 세션은 무상태
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 엔드포인트 권한
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                // Swagger/OpenAPI
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",

                                // OAuth/로그인 공개 경로
                                "/oauth2/**",
                                "/login/oauth2/**",

                                // 토큰 재발급
                                "/auth/refresh",

                                // 개발용
                                "/h2-console/**"
                        ).permitAll()
                        .anyRequest()
                        .authenticated()) // 나머지 요청은 인증 필요

                // 카카오 OAuth2 로그인: 사용자정보 서비스 + 성공 핸들러
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(kakaoOAuth2UserService) // 사용자 정보 조회 시 사용자 정의 서비스 사용
                        )
                        .successHandler(oAuth2SuccessHandler)) // Spring Security가 관리하는 OAuth2 로그인 흐름 안에서 토큰 생성 보장
                // JWT 필터는 UsernamePassrodAuthenticatorFilter 전에 하도록 함
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(denialHandler)
                )
                // H2 콘솔용 프레임 허용
                .headers(h -> h
                        .frameOptions(f -> f.sameOrigin()));

        return http.build();
    }

    @Bean
    @Profile("local")
    public SecurityFilterChain localSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // H2 콘솔은 CRSF 예외, 전반적으로는 비활성화
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                        .disable())
                // JWT 쓰므로 세션은 무상태
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 엔드포인트 권한 - 모든 요청 허용
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest()
                        .permitAll())
                // H2 콘솔용 프레임 허용
                .headers(h -> h
                        .frameOptions(f -> f.sameOrigin()));

        return http.build();
    }
}
