package com.example.alert.controller;

import com.example.alert.dtos.DeviceLogPowerConsumptionRequest;
import com.example.alert.dtos.DeviceLogRequest;
import com.example.alert.model.DeviceLog;
import com.example.alert.model.Users;
import com.example.alert.service.DeviceLogService;
import com.example.alert.service.Result;
import com.example.alert.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/logs")
public class DeviceLogController {
    @Autowired
    private DeviceLogService deviceLogService;
    @Autowired
    private UsersService usersService;
    @PostMapping()
    public void saveLog( @RequestBody DeviceLog deviceLog){
        deviceLogService.save(deviceLog);
    }
    @GetMapping
    public  void run() throws IOException {
        deviceLogService.getPower();
    }
    @GetMapping("/device-log")
    public ResponseEntity<?>getDeviceLogs(@RequestBody DeviceLogRequest deviceLogRequest){
        Result<List<DeviceLog>> result=deviceLogService.findDeviceLogByMonth(deviceLogRequest);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/power-consumption")
    public ResponseEntity<?> getPowerConsumption(@RequestBody DeviceLogPowerConsumptionRequest request) throws IOException {
        Authentication authentication =SecurityContextHolder.getContext().getAuthentication();
        Users user= (Users)authentication.getPrincipal();
        Users findUser=usersService.getUsersRepository().findByDeviceName(request.getDeviceName(),user.getUsername());
        if(findUser!=null){
            return  ResponseEntity.ok(deviceLogService.powerConsumption(request.getStartDate(),request.getEndDate(),request.getDeviceName()));
        }
        return ResponseEntity.notFound().build();
    }
}