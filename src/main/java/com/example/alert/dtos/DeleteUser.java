package com.example.alert.dtos;

public class DeleteUser {
    private Long deleteUserId;
    private String realDeviceId;
    private String token;
    public DeleteUser(Long deleteUserId,String realDeviceId,String token){
        this.deleteUserId=deleteUserId;
        this.realDeviceId=realDeviceId;
        this.token=token;
    }
    public DeleteUser(){}
    public Long getDeleteUserId() {
        return deleteUserId;
    }

    public void setDeleteUserId(Long deleteUserId) {
        this.deleteUserId = deleteUserId;
    }

    public String getRealDeviceId() {
        return realDeviceId;
    }

    public void setRealDeviceId(String realDeviceId) {
        this.realDeviceId = realDeviceId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
