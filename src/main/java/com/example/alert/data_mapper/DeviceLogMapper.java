package com.example.alert.data_mapper;

import com.example.alert.model.DeviceLog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DeviceLogMapper {
    public static DeviceLog mapper(String data){
        try{
            String[] parts = data.replace("\"", "").split(";");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss-dd/MM/yy");
            DeviceLog deviceLog = new DeviceLog();
            deviceLog.setDeviceLogId(parts[0]);
            deviceLog.setCreatedAt(LocalDateTime.parse(parts[1], formatter));
            deviceLog.setPowerFactor(Float.parseFloat(parts[2]));
            deviceLog.setVolt(Float.parseFloat(parts[3]));
            deviceLog.setAmpere(Float.parseFloat(parts[4]));
            return deviceLog;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
