package com.example.alert.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceLog {
    private LocalDateTime createdAt;
    private Float volt;
    private Float ampere;
    private Float powerFactor;
    private String deviceLogId;
    public  DeviceLog(
                              @JsonProperty("id") String deviceLogId,
                      @JsonProperty("createdAt") LocalDateTime createdAt,
                      @JsonProperty("cosPhi") Float powerFactor,
                      @JsonProperty("voltage") Float volt,
                      @JsonProperty("current") Float ampere){
        this.createdAt=createdAt;
        this.volt=volt;
        this.ampere=ampere;
        this.deviceLogId=deviceLogId;
        this.powerFactor=powerFactor;
    }
    public DeviceLog(){};

    public DeviceLog(LocalDateTime createdAt, Float powerFactor, Float volt, Float ampere) {
        this.createdAt=createdAt;
        this.powerFactor=powerFactor;
        this.volt=volt;
        this.ampere=ampere;
    }

    public Float getPowerFactor() {
        return powerFactor;
    }

    public Float getAmpere() {
        return ampere;
    }

    public void setAmpere(Float ampere) {
        this.ampere = ampere;
    }

    public Float getVolt() {
        return volt;
    }

    public void setVolt(Float volt) {
        this.volt = volt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setPowerFactor(Float powerFactor) {
        this.powerFactor = powerFactor;
    }

    public String getDeviceLogId() {
        return deviceLogId;
    }

    public void setDeviceLogId(String deviceLogId) {
        this.deviceLogId = deviceLogId;
    }
    @Override
    public String toString() {
        return "DeviceLog{" +
                "deviceLogId='" + deviceLogId + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", powerFactor=" + powerFactor +
                ", volt=" + volt +
                ", ampere=" + ampere +
                '}';
    }
}
