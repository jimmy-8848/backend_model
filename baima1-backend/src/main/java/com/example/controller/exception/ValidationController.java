package com.example.controller.exception;

import com.example.entity.RestBean;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ValidationController {
    @ExceptionHandler(ConstraintViolationException.class)
    public RestBean<Void> validateException(ValidationException e){
        log.warn("Resolve [{}:{}]",e.getClass().getName(),e.getMessage());
        return RestBean.failure(400,"请求参数有误");
    }
}
