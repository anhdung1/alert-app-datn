package com.example.alert.controller;

import com.example.alert.model.Users;
import com.example.alert.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/device")
@RestController
public class DeviceController {
    @Autowired
    private DeviceService deviceService;
    @GetMapping
    public ResponseEntity<?> getUserDevices(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user= (Users)authentication.getPrincipal();
        return ResponseEntity.ok(deviceService.
                findUserDeviceByUserId(user.getUsersId()));
    }
}
