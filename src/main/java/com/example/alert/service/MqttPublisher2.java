package com.example.alert.service;

import com.example.alert.consts.*;
import com.example.alert.dtos.*;
import com.example.alert.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.alert.data_mapper.DeviceLogMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class MqttPublisher2 {
    private final String BROKER_URL = "ssl://i1731e41.ala.asia-southeast1.emqxsl.com:8883";
    private final String CLIENT_ID = "spring-boot-client-234";
    @Autowired
    private DeviceLogService deviceLogService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AlertService alertService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private UsersInfoService usersInfoService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private FirebaseTokensService firebaseTokensService;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private MqttClient mqttClient;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final ObjectMapper objectMapper;
    final Map<String, List<Float>> sensorData = new HashMap<>();
    final Map<String,List<Float>> deltaMap=new HashMap<>();

    @Autowired
    public MqttPublisher2(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        startConnectionChecker();

    }
    private void connectToMqttBroker() {
        try {
            mqttClient = new MqttClient(BROKER_URL, CLIENT_ID);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("username");
            String password="password";
            options.setPassword(password.toCharArray());
            mqttClient.connect(options);
            System.out.println("Connected to MQTT broker successfully.");
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection lost: " + cause.getMessage());
                }
                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    messageReceived(topic,message);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });
            subscribeToTopics();
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());

        }
    }
    private void messageReceived(String topic, MqttMessage message){
        String json = new String(message.getPayload());
        System.out.println("Received message from topic " + topic + ": " + json);
        try {

            if(topic.equals(Topic.firebaseToken)){
                listenAndSaveFirebaseToken(json);
            }
            SecurityContextHolder.clearContext();
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            System.out.println(e.toString());
        }
    }
    // Set firebase token
    public void listenAndSaveFirebaseToken(String json) throws JsonProcessingException {
        FirebaseTokensRequest firebaseTokensRequest=objectMapper.readValue(json, FirebaseTokensRequest.class);
        boolean isValidateSuccess= jwtAuthenticationFilter.handleToken(firebaseTokensRequest.getToken());
        if(isValidateSuccess){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Users users=(Users)authentication.getPrincipal();
            FirebaseTokens firebaseTokens =new FirebaseTokens();
            firebaseTokens.setToken(firebaseTokensRequest.getToken());
            Users setUsers=usersService.findByUsername(users.getUsername());
            firebaseTokens.setUser(setUsers);
            firebaseTokensService.getFirebaseTokensRepository().save(firebaseTokens);
        }
    }
    private void subscribeToTopics() {
        try {
            List<String> topics = new ArrayList<>(Topic.getAllTopics());
            int maxIndex= Math.min(topics.size(), 19);
            for (int i=10;i<maxIndex;i++) {
                mqttClient.subscribe(topics.get(i), 0);
                System.out.println("Subscribed to topic: " + topics.get(i));
            }
        } catch (MqttException e) {
            System.out.println("Error subscribing to topics: " + e.getMessage());
        }
    }

    public MqttMessage setPayload(Object object) throws JsonProcessingException {
        MqttMessage mqttMessage=new MqttMessage();
        mqttMessage.setPayload(objectMapper.writeValueAsBytes(object));
        return mqttMessage;
    }

    private void startConnectionChecker() {
        scheduler.scheduleAtFixedRate(() -> {
            if (mqttClient == null || !mqttClient.isConnected()) {
                System.out.println("Đang kết nối tới Broker");
                connectToMqttBroker();
            }
        }, 0, 30, TimeUnit.SECONDS);
    }
}

