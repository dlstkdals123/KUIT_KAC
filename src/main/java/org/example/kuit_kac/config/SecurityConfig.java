package org.example.kuit_kac.config;

import org.example.kuit_kac.domain.oauth.service.KakaoOAuth2UserService;
import org.example.kuit_kac.exception.CustomAccessDenialHandler;
import org.example.kuit_kac.exception.CustomAuthenticationEntryPoint;
import org.example.kuit_kac.global.filter.DevKidAuthFilter;
import org.example.kuit_kac.global.filter.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http, DevKidAuthFilter devKidAuthFilter) throws Exception {
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

                                // 개발용
                                "/h2-console/**",
                                "/dev-tools/**",
                                "/dev-auth/**"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                // 토큰 재발급
                                "/auth/refresh"
                        ).permitAll()
                        .requestMatchers("/users/me").authenticated() //  테스트시 유일한 인증필요 api
//                        // TODO 개발 테스트 유저 삭제용 코드. 운영시 삭제!!
                        .requestMatchers(HttpMethod.DELETE, "/reset-user/**").permitAll() // ★ 추가
                        .anyRequest().permitAll()) // 테스트시 나머지는 요청 허가
//                        .authenticated()) // TODO: 나머지 요청은 인증 필요

                // 카카오 OAuth2 로그인: 사용자정보 서비스 + 성공 핸들러
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(ae -> ae.baseUri("/oauth2/authorization"))
                        .redirectionEndpoint(re -> re.baseUri("/login/oauth2/code/*"))
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(kakaoOAuth2UserService) // 사용자 정보 조회 시 사용자 정의 서비스 사용
                        )
                        .successHandler(oAuth2SuccessHandler)) // Spring Security가 관리하는 OAuth2 로그인 흐름 안에서 토큰 생성 보장
                // JWT 필터는 UsernamePassrodAuthenticatorFilter 전에 하도록 함
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(devKidAuthFilter, JwtAuthFilter.class)
                .addFilterAfter(devKidAuthFilter, JwtAuthFilter.class)                .addFilterAfter(devKidAuthFilter, JwtAuthFilter.class)                .addFilterAfter(jwtAuthFilter, DevKidAuthFilter.class)                .exceptionHandling(ex -> ex
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
    public SecurityFilterChain localSecurityFilterChain(HttpSecurity http, DevKidAuthFilter devKidAuthFilter) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**").disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/error",
                                "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**",
                                "/oauth2/**", "/login/oauth2/**",
                                "/h2-console/**", "/dev-tools/**", "/actuator/**"
                        ).permitAll()
                        .anyRequest().permitAll() // local은 전부 개방
                )
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(ae -> ae.baseUri("/oauth2/authorization"))
                        .redirectionEndpoint(re -> re.baseUri("/login/oauth2/code/*"))
                        .userInfoEndpoint(u -> u.userService(kakaoOAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                )

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(devKidAuthFilter, JwtAuthFilter.class)
                .headers(h -> h.frameOptions(f -> f.sameOrigin()));
        return http.build();
    }
}
