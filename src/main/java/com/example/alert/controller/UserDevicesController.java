package com.example.alert.controller;

import com.example.alert.model.Users;
import com.example.alert.service.Result;
import com.example.alert.service.UserDevicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-device")
public class UserDevicesController {
    @Autowired
    private UserDevicesService userDevicesService;
    @PostMapping("/create")
    public ResponseEntity<Result<?>> create(@RequestParam Long userId, @RequestParam int deviceId){
        Authentication authentication = SecurityContextHolder.createEmptyContext().getAuthentication();
        Users technical=(Users) authentication.getPrincipal();
        Result<?> result=userDevicesService.saveUserDevices(userId,technical.getRoles().getRole(),deviceId);
        return ResponseEntity.ok(result);
    }
}
