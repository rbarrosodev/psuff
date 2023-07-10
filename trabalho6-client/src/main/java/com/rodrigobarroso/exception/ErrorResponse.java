package com.rodrigobarroso.exception;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponse {
    private LocalDateTime dateTime;
    private int errorCode;
    private String error;
    private String metodo;
    private String requestURI;
    private Map<String, String> map;
    private String model;
    private String message;

    public ErrorResponse() {

    }
    public ErrorResponse(LocalDateTime dateTime, int errorCode, String error,
                         String metodo, String requestURI, Map<String, String> map,
                         String model, String message) {
        this.dateTime = dateTime;
        this.errorCode = errorCode;
        this.error = error;
        this.metodo = metodo;
        this.requestURI = requestURI;
        this.map = map;
        this.model = model;
        this.message = message;
    }
    @Override
    public String toString() {
        return "ErrorResponse{" +
                "dateTime=" + dateTime +
                ", errorCode=" + errorCode +
                ", error='" + error + '\'' +
                ", metodo='" + metodo + '\'' +
                ", requestURI='" + requestURI + '\'' +
                ", map=" + map +
                ", model='" + model + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    public int getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    public String getMetodo() {
        return metodo;
    }
    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }
    public String getRequestURI() {
        return requestURI;
    }
    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }
    public Map<String, String> getMap() {
        return map;
    }
    public void setMap(Map<String, String> map) {
        this.map = map;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}