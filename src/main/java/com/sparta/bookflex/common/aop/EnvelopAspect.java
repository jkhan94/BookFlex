package com.sparta.bookflex.common.aop;

import com.sparta.bookflex.common.exception.exceptionDto.EnvelopeResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class EnvelopAspect {

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object envelopResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        com.sparta.bookflex.common.aop.Envelop envelopAnnotation = method.getAnnotation(com.sparta.bookflex.common.aop.Envelop.class);

        String msg = getMessage(envelopAnnotation, method.getName());

        Object result = joinPoint.proceed();

        if (!(result instanceof ResponseEntity<?> responseEntity)) {
            return result;
        }

        return EnvelopeResponse.wrap(
                responseEntity.getBody(),
                HttpStatus.valueOf(responseEntity.getStatusCode().value()),
                msg
        );
    }

    private String getMessage(com.sparta.bookflex.common.aop.Envelop envelopAnnotation, String name) {
        if (envelopAnnotation == null) {
            return name + " 완료";
        }
        return envelopAnnotation.value();
    }
}
