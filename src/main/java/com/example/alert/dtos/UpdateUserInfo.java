package com.example.alert.dtos;

import com.example.alert.model.UsersInfo;

public class UpdateUserInfo {
    private String address;
    private String fullName;
    private String email;
    private String phone;
    private String imageUrl;
    private Long usersId;
    private String token;
    private String realDeviceId;
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public Long getUsersId() {
        return usersId;
    }

    public void setUsersId(Long usersId) {
        this.usersId = usersId;
    }

    public String getToken() {
        return token;
    }

    public String getRealDeviceId() {
        return realDeviceId;
    }
}
