package com.location.validator.exception;

import com.location.validator.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GenericExceptionHandler  {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataNotFoundException.class)
    ErrorMessage dataNotFoundException(DataNotFoundException ex) {
        ErrorMessage errorMsg= new ErrorMessage(HttpStatus.BAD_REQUEST,ex.getMessage());
        return errorMsg;
    }
}
