package com.example.alert.controller;

import com.example.alert.dtos.AuthRequest;
import com.example.alert.dtos.AuthResponse;
import com.example.alert.model.Users;
import com.example.alert.model.UsersInfo;
import com.example.alert.service.JwtUtil;
import com.example.alert.service.MqttPublisher;
import com.example.alert.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication=authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            UserDetails userDetails= (UserDetails)authentication.getPrincipal();
            Users user= usersService.findByUsername(userDetails.getUsername());
            UsersInfo usersInfo=user.getUsersInfo();
            String role=authentication.getAuthorities().iterator().next().getAuthority();
            String token = jwtUtil.generateToken(authRequest.getUsername(), role);
//            return  ResponseEntity.ok(authentication.getPrincipal());
            return ResponseEntity.ok(new AuthResponse(role,user.getUsername(),usersInfo.getEmail(),usersInfo.getImageUrl(),usersInfo.getFullName(),usersInfo.getAddress(),user.getUsersId(),token));
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
    @PostMapping("/mqtt-login")
    public ResponseEntity<?> loginWithMqtt(@RequestBody String json){
        boolean isSuccess=  mqttPublisher.publishLogin(json);
        if(isSuccess)return ResponseEntity.ok("");
        return ResponseEntity.notFound().build();
    }
}
