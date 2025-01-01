package com.example.alert.service;

public class Result<T> {
    private T data;
    private String message;
    private int statusCode;

    public Result(T data, String message, int statusCode) {
        this.data = data;
        this.message = message;
        this.statusCode = statusCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 300; // 2xx là thành công
    }
}

