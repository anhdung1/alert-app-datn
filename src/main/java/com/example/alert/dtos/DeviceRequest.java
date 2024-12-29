package com.example.alert.dtos;

public class DeviceRequest {
    private String deviceId;
    private Long userId;
    public DeviceRequest(String deviceId,Long userId){
        this.deviceId=deviceId;
        this.userId=userId;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
