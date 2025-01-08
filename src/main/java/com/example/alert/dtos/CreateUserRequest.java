package com.example.alert.dtos;

public class CreateUserRequest {
    private String username;
    private String password;
    private String phone;
    private String realDeviceId;
    private String token;

    public String getRealDeviceId() {
        return realDeviceId;
    }

    public void setRealDeviceId(String realDeviceId) {
        this.realDeviceId = realDeviceId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
