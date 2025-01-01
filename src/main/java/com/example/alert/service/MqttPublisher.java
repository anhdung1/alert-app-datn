package com.example.alert.service;

import com.example.alert.dtos.*;
import com.example.alert.model.DeviceLog;
import com.example.alert.model.Users;
import com.example.alert.model.UsersInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.example.alert.data_mapper.DeviceLogMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class MqttPublisher {
    private final String BROKER_URL = "ssl://i1731e41.ala.asia-southeast1.emqxsl.com:8883";
    private final String CLIENT_ID = "spring-boot-client-dung";
    private final String loginTopic="login/client";
    private final String historyTopic="history/client";
    private final String deviceTopic="device/client";
    private final String deviceLogTopic="device-log/client";
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
    private MqttClient mqttClient;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    public MqttPublisher() {
        startConnectionChecker();
    }
    private void connectToMqttBroker() {
        try {
            mqttClient = new MqttClient(BROKER_URL, CLIENT_ID);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName("dungdung");
            String password="12345678";
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
            if(topic.equals(loginTopic)){
               listenMessageLoginTopic(json);
            }
            if(topic.equals(historyTopic)){
                listenAndPublishAlert(json);
            }
            if(topic.equals(deviceTopic)){
                listenAndPublishDevice(json);
            }
            if(topic.equals(deviceLogTopic)){
                listenAndSaveDeviceLog(json);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    // Nghe và gửi thiết bị theo user
    public void listenAndPublishDevice(String json) throws JsonProcessingException, MqttException {
        DeviceRequest deviceRequest=objectMapper.readValue(json,DeviceRequest.class);
        List<DeviceResponse> deviceResponse=deviceService.getDeviceRepository().findUserDeviceByUserId(deviceRequest.getUserId());
        mqttClient.publish(deviceTopic+"/"+deviceRequest.getDeviceId(),setMqttMessage(deviceResponse));
    }
    // Nghe và gửi lịch sử cảnh báo
    private void listenAndPublishAlert(String json) throws JsonProcessingException, MqttException {
        AlertRequest alertRequest=objectMapper.readValue(json, AlertRequest.class);
        Result<List<AlertResponse>> result=alertService.getAlertsByTimeAndType(alertRequest);
        MqttMessage mqttMessage=new MqttMessage();
        if(result.isSuccess()){
            mqttMessage.setPayload(objectMapper.writeValueAsBytes(result.getData()));
            mqttClient.publish(historyTopic+"/"+alertRequest.getDeviceId(),mqttMessage);
        }
        String error= result.getMessage();
        mqttMessage.setPayload(error.getBytes());

    }
    // Nghe và lưu dữ liệu device log
    public  void listenAndSaveDeviceLog(String json){
        DeviceLog deviceLog = DeviceLogMapper.mapper(json);
        deviceLogService.save(deviceLog);
    }
    public MqttMessage setMqttMessage(Object object) throws JsonProcessingException {
        MqttMessage mqttMessage=new MqttMessage();
        mqttMessage.setPayload(objectMapper.writeValueAsBytes(object));
        return mqttMessage;
    }
    // Nghe và gửi dữ liệu login
    private void listenMessageLoginTopic(String json) throws JsonProcessingException, MqttException {
        AuthRequest authRequest = objectMapper.readValue(json,AuthRequest.class);
        MqttMessage mqttMessage=new MqttMessage();
       try{
           Authentication authentication=authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
           System.out.println(authentication.getPrincipal());
           UserDetails userDetails=(UserDetails)authentication.getPrincipal();
           String role=authentication.getAuthorities().iterator().next().getAuthority();
           Users user=usersService.findByUsername(userDetails.getUsername());
           UsersInfo usersInfo=user.getUsersInfo();
           AuthResponse authResponse=new AuthResponse(role,user.getUsername(),
                   usersInfo.getEmail(),
                   usersInfo.getImageUrl(),
                   usersInfo.getFullName(),
                   usersInfo.getAddress(),
                   user.getUsersId(),"");

           mqttMessage.setPayload(objectMapper.writeValueAsBytes(authResponse));
           mqttClient.publish(loginTopic+"/"+ authRequest.getDeviceId(),mqttMessage);}
       catch (Exception e) {
           String message="Thông tin tài khoản hoặc mật khẩu sai";
           mqttMessage.setPayload(message.getBytes());
           mqttClient.publish(loginTopic+"/"+ authRequest.getDeviceId(),mqttMessage);
       }
    }
    // Thử publish
    public boolean  publish(AlertRequest alertRequest)  {
        MqttMessage mqttMessage=new MqttMessage();
       try{
           objectMapper.registerModule(new JavaTimeModule());
           mqttMessage.setPayload(objectMapper.writeValueAsBytes(alertRequest));
           mqttClient.publish(historyTopic,mqttMessage);
           return  true;
       } catch (Exception e) {
           System.out.println(e.toString());
           return false;
       }
    }
    public boolean  publishLogin(String json)  {
        MqttMessage mqttMessage=new MqttMessage();
        try{
            objectMapper.registerModule(new JavaTimeModule());
            mqttMessage.setPayload(json.getBytes());
            mqttClient.publish(deviceLogTopic,mqttMessage);
            return  true;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }
    private void subscribeToTopics() {
        try {
            List<String> topics =new ArrayList<>();
            topics= List.of("login/client","login/client/1234","history/client","history/client/1234","device/client",deviceLogTopic);
            for (String topic : topics) {

                mqttClient.subscribe(topic, 0);
                System.out.println("Subscribed to topic: " + topic);
            }
        } catch (MqttException e) {
            System.out.println("Error subscribing to topics: " + e.getMessage());
        }
    }


//    private void startConnectionCheckerToTopic() {
//        scheduler.scheduleAtFixedRate(() -> {
//            long currentTime = System.currentTimeMillis();
//            lastMessageTimeByTopic.entrySet().removeIf(entry -> {
//                String topic = entry.getKey();
//                long messageTime = entry.getValue();
//                if (currentTime - messageTime > 30000) {
//                    System.out.println("Lost connection to topic: " + topic);
//                    try {
//                        mqttClient.subscribe(topic, 0);
//                    } catch (MqttException e) {
//                        System.out.println("Failed to reconnect to topic: " + topic);
//                    }
//                    return true;
//                }
//                return false;
//            });
//        }, 0, 30, TimeUnit.SECONDS);
//    }

    private void startConnectionChecker() {
        scheduler.scheduleAtFixedRate(() -> {
            if (mqttClient == null || !mqttClient.isConnected()) {
                System.out.println("Đang kết nối tới Broker");
                connectToMqttBroker();
            }
        }, 0, 30, TimeUnit.SECONDS);
    }
}
