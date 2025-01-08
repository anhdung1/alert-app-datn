package com.example.alert.dtos;

import java.time.LocalDateTime;

public class DeviceLogPowerConsumptionRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String deviceName;
    private String realDeviceId;
    private String token;
    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
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
