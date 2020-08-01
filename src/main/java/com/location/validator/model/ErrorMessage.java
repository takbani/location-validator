package com.location.validator.model;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ErrorMessage {

    private HttpStatus status;
    private String message;


    public ErrorMessage(){

    }

    public ErrorMessage(HttpStatus status,String message,List<String> errors){
        this.status=status;
        this.message=message;
    }
    public ErrorMessage(HttpStatus status,String message){
        this.status=status;
        this.message=message;
    }
    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
