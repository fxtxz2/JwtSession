package com.example.demo.exception;

import com.example.demo.comm.Result;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;

/**
 * 全局异常
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionTranslator {
    @ExceptionHandler(DemoException.class)
    public ResponseEntity<Result> recsException(DemoException demoException){
        Result result = Result.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(demoException.getMessage())
                .build();
        return ResponseEntity.ok().body(result);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result> handleError(MethodArgumentNotValidException e) {
        log.warn("Method Argument Not Valid", e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String message = null;
        if (error != null) {
            message = String.format("%s:%s", error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest()
                .body(Result.builder().code(HttpStatus.BAD_REQUEST.value()).message(message).build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Result> handleError(ConstraintViolationException e) {
        log.warn("Constraint Violation", e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String path = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
        String message = String.format("%s:%s", path, violation.getMessage());
        return ResponseEntity.badRequest()
                .body(Result.builder().code(HttpStatus.BAD_REQUEST.value()).message(message).build());
    }

}
