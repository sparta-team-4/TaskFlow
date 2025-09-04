package com.sparta.taskflow.common.security;


import com.sparta.taskflow.common.enums.Role;
import com.sparta.taskflow.common.exception.FilterExceptionHandler;
import com.sparta.taskflow.common.exception.server.ServerErrorCode;
import com.sparta.taskflow.common.properties.JwtSecurityProperties;
import com.sparta.taskflow.common.security.model.CustomUserAuthentication;
import com.sparta.taskflow.common.utils.JwtUtils;
import com.sparta.taskflow.domain.auth.exception.AuthErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtSecurityProperties jwtSecurityProperties;
    private final JwtUtils jwtUtils;
    private final FilterExceptionHandler filterExceptionHandler;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        List<String> whiteList = jwtSecurityProperties.getSecret().getWhiteList();

        for (String pattern : whiteList) {
            if (pathMatcher.match(pattern, requestURI)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        log.info("권한이나 인증이 필요한 요청 {}", request.getRequestURI());
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!StringUtils.hasText(bearerToken)) {
            filterExceptionHandler.sendErrorResponse(request, response, AuthErrorCode.TOKEN_REQUIRED.getHttpStatus(), AuthErrorCode.TOKEN_REQUIRED.getMessage());
            return;
        }

        String token = jwtUtils.substringToken(bearerToken);

        try {
            Claims claims = jwtUtils.extractClaims(token);
            if (claims == null || claims.getSubject() == null) {
                filterExceptionHandler.sendErrorResponse(request, response, AuthErrorCode.INVALID_TOKEN.getHttpStatus(), AuthErrorCode.INVALID_TOKEN.getMessage());
                return;
            }

            Long userId = extractUserId(claims);
            Role role = extractRole(claims);

            // Authentication(인증) 객체 생성
            Authentication authentication = new CustomUserAuthentication(userId, role);

            // SecurityContext 에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
            filterExceptionHandler.sendErrorResponse(request, response, AuthErrorCode.TOKEN_EXPIRED.getHttpStatus(), AuthErrorCode.TOKEN_EXPIRED.getMessage());
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않은 JWT 서명 입니다.", e);
            filterExceptionHandler.sendErrorResponse(request, response, AuthErrorCode.INVALID_SIGNATURE.getHttpStatus(), AuthErrorCode.INVALID_SIGNATURE.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
            filterExceptionHandler.sendErrorResponse(request, response, AuthErrorCode.UNSUPPORTED_TOKEN.getHttpStatus(), AuthErrorCode.UNSUPPORTED_TOKEN.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Invalid JWT token, 유효하지 않은 JWT 토큰 입니다.", e);
            filterExceptionHandler.sendErrorResponse(request, response, AuthErrorCode.MALFORMED_TOKEN.getHttpStatus(), AuthErrorCode.MALFORMED_TOKEN.getMessage());
        } catch (Exception e) {
            log.error("예상치 못한 예외 발생", e);
            filterExceptionHandler.sendErrorResponse(request, response, ServerErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus(), ServerErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        }
    }

    private Long extractUserId(Claims claims) {
        String[] decryptedData = claims.getSubject().split(":");
        return Long.valueOf(decryptedData[0]);
    }

    private Role extractRole(Claims claims) {
        String[] decryptedData = claims.getSubject().split(":");
        return Role.valueOf(decryptedData[1]);
    }
}
