package io.swagger.model.dto;

import org.springframework.http.HttpStatus;

import java.util.Date;


public class ExceptionDTO {

    private int statusCode;
    private String statusMsg;


    private String exceptionMsg;
    private String path;
    private Date date;

    public ExceptionDTO(HttpStatus httpStatus, String msg, String path) {
        this.statusCode = httpStatus.value();
        this.statusMsg = httpStatus.name();
        this.exceptionMsg = msg;
        this.path = path;
        this.date = new Date();
    }

    public ExceptionDTO(HttpStatus httpStatus, String msg) {
        this.statusCode = httpStatus.value();
        this.statusMsg = httpStatus.name();
        this.exceptionMsg = msg;
        this.date = new Date();
    }


    public ExceptionDTO(String msg) {
        this.exceptionMsg = msg;
        this.date = new Date();
    }

    @Override
    public String toString() {
        Integer statusCode = this.statusCode;
        String statusMsg = this.statusMsg;
        String message = this.exceptionMsg;
        return "{\n  \"error\": \"" + statusCode + "\",\n  \"statusName\": \"" + statusMsg + "\", \n  \"message\": \"" + message + "\"\n}";
    }
}