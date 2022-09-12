package br.com.springbootapi.exception.handler;

import br.com.springbootapi.exception.RequestException;
import br.com.springbootapi.exception.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(RequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponse requestExceptionHandler(RequestException exception) {
        return new ExceptionResponse(exception.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ExceptionResponse usernameNotFoundExceptionHandler(UsernameNotFoundException exception) {
        return new ExceptionResponse(exception.getMessage());
    }
}