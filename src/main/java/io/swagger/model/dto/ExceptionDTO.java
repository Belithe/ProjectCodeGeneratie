package io.swagger.model.dto;

import org.springframework.http.HttpStatus;

import java.util.Date;


public class ExceptionDTO {

    private int statusCode;
    private String statusMsg;
    private HttpStatus httpStatus;

    private String exceptionMsg;
    private String path;
    private Date date;

    public ExceptionDTO(HttpStatus httpStatus, String msg, String path) {
        this.httpStatus = httpStatus;
        this.statusCode = httpStatus.value();
        this.statusMsg = httpStatus.name();
        this.exceptionMsg = msg;
        this.path = path;
        this.date = new Date();
    }

    public ExceptionDTO(HttpStatus httpStatus, String msg) {
        this.httpStatus = httpStatus;
        this.statusCode = httpStatus.value();
        this.statusMsg = httpStatus.name();
        this.exceptionMsg = msg;
        this.date = new Date();
    }

    public ExceptionDTO(String msg) {
        this.exceptionMsg = msg;
        this.date = new Date();
    }

    public ExceptionDTO(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.statusCode = httpStatus.value();
        this.statusMsg = httpStatus.name();

        if (this.statusCode == 401) {
            this.exceptionMsg = "You are not authorized to execute this command.";
        }
        else if (this.statusCode == 403) {
            this.exceptionMsg = "No authentication token was given.";
        }
        this.date = new Date();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public String toString() {
        Integer statusCode = this.statusCode;
        String message = this.exceptionMsg;
        return "{\n  \"error\": \"" + statusCode + "\", \n  \"message\": \"" + message + "\"\n}";
    }

}