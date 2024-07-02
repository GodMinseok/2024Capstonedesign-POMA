package goinmul.sportsmanage.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException() {
        return "에러가 발생했습니다";
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public String NoResourceFoundException() {
        return "존재하지 않는 페이지입니다";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String DataIntegrityViolationException() {
        return "중복된 데이터를 추가했습니다";
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String MissingServletRequestParameterException() {
        return "에러가 발생했습니다";
    }
}
