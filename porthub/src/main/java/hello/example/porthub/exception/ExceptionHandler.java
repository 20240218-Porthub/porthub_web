package hello.example.porthub.exception;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public String handleConstraint(SQLIntegrityConstraintViolationException e) {
        return "error/404";
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(PersistenceException.class)
    public String handlePersistenceException(PersistenceException e) {
        log.error("Database error occurred", e);
        return "error/500";  // 적절한 에러 페이지로 리디렉션
    }
}
