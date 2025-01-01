package com.example.alert.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer deviceId;
    private String model;
    private String version;
    private String deviceName;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "device")
    private List<UserDevices>userDevices;
    @OneToMany(mappedBy = "device")
    private List<Alert> alerts;
    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<UserDevices> getUserDevices() {
        return userDevices;
    }

    public void setUserDevices(List<UserDevices> userDevices) {
        this.userDevices = userDevices;
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
