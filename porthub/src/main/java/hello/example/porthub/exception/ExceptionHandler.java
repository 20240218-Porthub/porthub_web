package hello.example.porthub.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public String handleConstraint(SQLIntegrityConstraintViolationException e) {
        return "error/404";
    }
}
