package com.example.accountingsystem.aspect;

import com.example.accountingsystem.annotation.Logger;
import com.example.accountingsystem.security.SecurityUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Objects;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    private static final String LOG_MESSAGE = "User with email {} send a request " +
            "to the {} endpoint via the {} http method and the call method {} " +
            "{} the table {}";

    @Pointcut("@annotation(com.example.accountingsystem.annotation.Logger)")
    private static void loggerAnnotation() {
    }

    @Pointcut("within(com.example.accountingsystem.service..*)")
    private static void allExceptionPointCut() {}

    @After("loggerAnnotation()")
    public static void logAfter(JoinPoint joinPoint) {
        String username = getUsername();
        String message = "";
        String tableName = "";
        var request = getServletRequest();
        var methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.toString();
        String endPoint = request.getRequestURL().toString();
        String httpMethod = request.getMethod();
        Method method = methodSignature.getMethod();
        if (method.isAnnotationPresent(Logger.class)) {
            Logger logger = method.getAnnotation(Logger.class);
            message = logger.message();
            tableName = logger.tableName();
        }
        log.info(LOG_MESSAGE, username, endPoint, httpMethod, methodName, message, tableName);
    }

    @AfterThrowing(pointcut = "loggerAnnotation() || allExceptionPointCut()", throwing = "exception")
    public static void logAfterThrowing(Throwable exception) {
        log.error("Exception occurred: {}", exception.toString());
    }

    private static String getUsername() {
        return ((SecurityUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername();
    }

    private static HttpServletRequest getServletRequest() {
        return ((ServletRequestAttributes)
                Objects.requireNonNull(RequestContextHolder
                        .getRequestAttributes()))
                .getRequest();
    }
}
