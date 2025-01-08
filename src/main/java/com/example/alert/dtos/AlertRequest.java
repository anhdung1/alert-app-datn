package com.example.alert.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

public class AlertRequest {
    private Long userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startHour;
    private LocalTime endHour;
    private Integer pageNumber;
    private Integer pageSize;
    private String realDeviceId;
    private String token;

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getStartHour() {
        return startHour;
    }

    public void setStartHour(LocalTime startHour) {
        this.startHour = startHour;
    }

    public LocalTime getEndHour() {
        return endHour;
    }

    public void setEndHour(LocalTime endHour) {
        this.endHour = endHour;
    }

    public Integer getPageNumber() {
        if(pageNumber!=null)return pageNumber;
        return 0;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        if(pageSize!=null)return pageSize;
        return 0;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getRealDeviceId() {
        return realDeviceId;
    }

    public void setRealDeviceId(String realDeviceId) {
        this.realDeviceId = realDeviceId;
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
}
