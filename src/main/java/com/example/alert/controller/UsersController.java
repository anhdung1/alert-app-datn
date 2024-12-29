package com.example.alert.controller;

import com.example.alert.dtos.AuthRequest;
import com.example.alert.model.Users;
import com.example.alert.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UsersService usersService;
    @PostMapping("/create-sub-user")
    public ResponseEntity<?> createSubUser(@RequestBody AuthRequest authRequest){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Users users=(Users)authentication.getPrincipal();
        boolean isExists=usersService.getUsersRepository().existsByUsername(authRequest.getUsername());
        if(isExists) return ResponseEntity.status(409).body("Username already exists");
        usersService.createSubUser(authRequest.getUsername(),
                authRequest.getPassword(),
                users.getUsersId(),
                authRequest.getPhone());
        return ResponseEntity.ok("User created successfully");
    }
    @PostMapping("/register-sub-user")
    public  ResponseEntity<?>registerSubUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
//        Users users=(Users)authentication.getPrincipal();
        return ResponseEntity.ok(authentication.getPrincipal());
//        boolean isExists=usersService.getUsersRepository().existsByUsername(authRequest.getUsername());
//        if(isExists){
//            return ResponseEntity.status(401).body("Username already exists");
//        }
//        usersService.createSubUser(authRequest.getUsername(), authRequest.getPassword(),,authRequest.getPhone());
//        return ResponseEntity.ok("User registered successfully");

    }
    //    @PostMapping("/set-device-sub-user")
//    public ResponseEntity<?> setDeviceSubUser(){
//
//    }
}
