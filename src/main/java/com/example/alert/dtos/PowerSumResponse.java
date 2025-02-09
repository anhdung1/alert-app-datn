package com.example.alert.dtos;

import java.time.LocalDate;

public class PowerSumResponse {
    private LocalDate date;
    private float powerSum;

    public float getPowerSum() {
        return powerSum;
    }

    public void setPowerSum(float powerSum) {
        this.powerSum = powerSum;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
