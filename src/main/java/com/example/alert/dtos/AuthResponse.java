package com.example.alert.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthResponse {
    @JsonProperty("role")
    private String role;
    @JsonProperty("username")
    private String username;
    private String email;
    private String imageUrl;
    private String fullName;
    private String address;
    private String phoneNumber;
    private Long userId;
    private String token;
    public AuthResponse(String role,String username,String email, String imageUrl, String fullName, String address,Long userId,String token,String phoneNumber){
        this.role=role;
        this.phoneNumber=phoneNumber;
        this.username=username;
        this.userId=userId;
        this.address=address;
        this.email=email;
        this.fullName=fullName;
        this.imageUrl=imageUrl;
        this.token=token;
    }
    public AuthResponse(String role,String username){
        this.role=role;
        this.username=username;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
