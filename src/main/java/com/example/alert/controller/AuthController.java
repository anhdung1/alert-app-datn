package com.example.alert.controller;

import com.example.alert.dtos.AlertRequest;
import com.example.alert.dtos.AuthRequest;
import com.example.alert.dtos.AuthResponse;
import com.example.alert.model.Users;
import com.example.alert.model.UsersInfo;
import com.example.alert.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private MqttPublisher mqttPublisher;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UsersService usersService;
    @Autowired
    private DeviceLogService deviceLogService;
    @Autowired
    private Analyzer analyzer;
    @Autowired
    private FirebaseTokensService firebaseTokensService;
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication=authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            UserDetails userDetails= (UserDetails)authentication.getPrincipal();
            Users user= usersService.findByUsername(userDetails.getUsername());
            UsersInfo usersInfo=user.getUsersInfo();
            String role=authentication.getAuthorities().iterator().next().getAuthority();
            String token = jwtUtil.generateToken(authRequest.getUsername(), role,user.getUsersId());
//            return  ResponseEntity.ok(authentication.getPrincipal());
            return ResponseEntity.ok(new Result<>(new AuthResponse(role,user.getUsername(),usersInfo.getEmail(),usersInfo.getImageUrl(),usersInfo.getFullName(),usersInfo.getAddress(),user.getUsersId(),token,usersInfo.getPhone()),"",200));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Username or password is incorrect");
        }
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest authRequest) {

        boolean isExists=usersService.getUsersRepository().existsByUsername(authRequest.getUsername());
        if(isExists){
            return ResponseEntity.status(401).body("Username already exists");
        }
        usersService.createUser(authRequest.getUsername(), authRequest.getPassword(),authRequest.getPhone());
        return ResponseEntity.ok("User registered successfully");
    }
    @GetMapping("/test")
    public ResponseEntity<?> test(){
//        deviceLogService.convertTxtToJson();
//        analyzer.sendData();
        List<String> deviceToken=firebaseTokensService.getFirebaseTokensRepository().findFirebaseTokenByDeviceName("0000000002");
        return ResponseEntity.ok(deviceToken);
    }
}
