package com.example.alert.dtos;

import com.example.alert.service.DeviceService;

public class DeviceResponse {
    private Integer deviceId;
    private String model;
    private String version;
    public DeviceResponse(Integer deviceId,String model,String version){
        this.deviceId=deviceId;
        this.version=version;
        this.model=model;
    }
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }
}
