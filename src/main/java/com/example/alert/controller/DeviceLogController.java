package com.example.alert.controller;

import com.example.alert.model.DeviceLog;
import com.example.alert.service.DeviceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/logs")
public class DeviceLogController {
    @Autowired
    private DeviceLogService deviceLogService;
    @PostMapping()
    public void saveLog( @RequestBody DeviceLog deviceLog){
        deviceLogService.save(deviceLog);
    }
    @GetMapping
    public  void run() throws IOException {
        deviceLogService.getPower();
    }
    @GetMapping("/device-log")
    public ResponseEntity<?>getDeviceLogs(){
        return ResponseEntity.ok(deviceLogService.findDeviceLogByMonth(11,2024,"0000000003"));
    }
}