package com.example.alert.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthRequest {
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("deviceId")
    private String deviceId;
    @JsonProperty("phone")
    private String phone;
    public AuthRequest(String username, String password, String deviceId,String phone){
        this.deviceId=deviceId;
        this.username=username;
        this.password=password;
        this.phone=phone;
    }
    public AuthRequest(){};
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
