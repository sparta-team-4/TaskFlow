package com.sparta.taskflow.common.logs.aspect;

import com.sparta.taskflow.common.logs.exception.AuthenticationRequiredException;
import com.sparta.taskflow.common.logs.exception.LogErrorCode;
import com.sparta.taskflow.common.security.model.CustomUserAuthentication;
import com.sparta.taskflow.domain.user.entity.User;
import com.sparta.taskflow.domain.user.service.external.UserExternalService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggableAspect {

    private final Logger log = LoggerFactory.getLogger(LoggableAspect.class);

    @Pointcut("@annotation(com.sparta.taskflow.common.logs.annotation.Loggable)")
    private void loggableMethods() {}

    @Around("loggableMethods()")
    public Object log(ProceedingJoinPoint pjp) throws Throwable {

        CustomUserAuthentication authentication = (CustomUserAuthentication) SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new AuthenticationRequiredException(LogErrorCode.AUTHENTICATION_REQUIRED);
        }

        Long userId = authentication.getUserId();

        long startTime = System.currentTimeMillis();

        String methodName = pjp.getSignature().toShortString();
        Object[] args = pjp.getArgs();

        String traceId = MDC.get("traceId");

        try {
            log.info("[{}] -> user={} {} args={}", traceId, userId, methodName, args);

            Object result = pjp.proceed();

            log.info("[{}] <- {} retType={}", traceId, methodName,
                    (result == null ? "void" : result.getClass().getSimpleName()));

            return result;
        } catch (Throwable t) {
            log.error("[{}] !! {} ex={} msg={}", traceId, methodName,
                    t.getClass().getSimpleName(), t.getMessage());
            throw t;
        } finally {
            log.info("[{}] took={}ms", traceId, System.currentTimeMillis() - startTime);
        }
    }
}
