package com.sparta.taskflow.common.security;

import com.sparta.taskflow.common.properties.JwtSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtSecurityProperties jwtSecurityProperties;

    /**
     * HTTP에 대해서 ‘인증’과 ‘인가’를 담당하는 메서드이며 필터를 통해 인증 방식과 인증 절차에 대해서 등록하며 설정을 담당하는 메서드
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))// 세션 사용 안함
                .formLogin(AbstractHttpConfigurer::disable) // 기본 로그인 폼 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // http basic 인증 비활성화, Basic 인증은 사용자 이름과 비밀번호를 Base64로 인코딩하여 인증값으로 활용
                .headers(headers -> headers
                        .xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.from("1; mode=block"))) // XSS 공격 방지
                        .contentTypeOptions(HeadersConfigurer.ContentTypeOptionsConfig::disable) // MIME 스니핑 방지
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin) // ClickJacking 방지
                        .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER)) // Referrer 비활성화
                        .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'; script-src 'self' 'nonce-randomValue'"))// XSS 방지
                );
        http
                .authorizeHttpRequests(auth -> auth
                        // 정적 자원에 대해서 Security를 적용하지 않음으로 설정
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        // 특정 url 패턴에 대해서는 인증처리(Authentication 객체 생성) 제외
                        .requestMatchers(jwtSecurityProperties.getSecret().getWhiteList().toArray(new String[0])).permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    /**
     * CORS에 대한 설정을 커스텀으로 구성
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        /**
         * setAllowCredentials(true) 일 경우 setAllowedOrigins("*") 은 exception
         * 참고 : https://jangjeonghun.tistory.com/1204
         * configuration.setAllowedOrigins(List.of("http://localhost:3000")); // 허용할 오리진
         */
        configuration.setAllowedOriginPatterns(List.of("*"));      // TODO: 추후 정확한 도메인을 입력하여 위 방식으로 수정
        configuration.setAllowedMethods(List.of("*"));      // 허용할 HTTP 메서드, 모든 HTTP 메서드 허용
        configuration.setAllowedHeaders(List.of("*"));      // 모든 헤더 허용
        configuration.setAllowCredentials(true);                // 자격 인증 정보 허용
        configuration.setMaxAge(3600L);                         // 프리플라이트 요청 결과를 3600초 동안 캐시

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 URL 패턴에 대해서 cors 허용 설정
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
