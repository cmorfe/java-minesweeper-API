package com.cmorfe.minesweeper.advice;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
    public class UsernameNotFoundAdvice {
        @ResponseBody
        @ExceptionHandler(UsernameNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        ResponseMessage usernameNotFoundHandler(UsernameNotFoundException ex) {
            return new ResponseMessage(ex.getMessage());
        }
    }