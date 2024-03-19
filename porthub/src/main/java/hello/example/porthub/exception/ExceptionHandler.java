package hello.example.porthub.exception;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public String handleConstraint(SQLIntegrityConstraintViolationException e) {
        return "error/404";
    }


}
