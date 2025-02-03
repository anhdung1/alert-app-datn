package com.example.alert.service;

import com.example.alert.consts.ErrorMessage;
import com.example.alert.consts.Topic;
import com.example.alert.dtos.*;
import com.example.alert.model.DeviceLog;
import com.example.alert.model.UserDevices;
import com.example.alert.model.Users;
import com.example.alert.model.UsersInfo;
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
public class MqttPublisher {
    private final String BROKER_URL = "ssl://i1731e41.ala.asia-southeast1.emqxsl.com:8883";
    private final String CLIENT_ID = "spring-boot-client-23";
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
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private MqttClient mqttClient;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final ObjectMapper objectMapper;


    @Autowired
    public MqttPublisher(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        startConnectionChecker();

    }
    private void connectToMqttBroker() {
        try {
            mqttClient = new MqttClient(BROKER_URL, CLIENT_ID);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName("emqx");
            String password="dungdung";
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

            if(topic.equals(Topic.loginTopic)){
               listenMessageLoginTopic(json);
            }
            if(topic.equals(Topic.historyTopic)){
                listenAndPublishAlert(json);
            }
            if(topic.equals(Topic.deviceTopic)){
                listenAndPublishDevice(json);
            }
            if(topic.equals(Topic.deviceLogTopic)){
                listenAndSaveDeviceLog(json);
            }
            if(topic.equals(Topic.powerConsumption)){
                listenAndPublishPowerConsumption(json);
            }
            if(topic.equals(Topic.createUserTopic)){
                listenAndPublishCreateUser(json);
            }
            if(topic.equals(Topic.editUserTopic)){
                listenAndPublishEditUserInfo(json);
            }
            if(topic.equals(Topic.changePasswordTopic)){
                listenAndUpdatePassword( json);
            }
            SecurityContextHolder.clearContext();
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            System.out.println(e.toString());
        }
    }
    // Nghe và update password
    public void listenAndUpdatePassword(String json) throws JsonProcessingException, MqttException {
        UpdatePassword updatePassword=objectMapper.readValue(json,UpdatePassword.class);
        boolean isValidateSuccess= jwtAuthenticationFilter.handleToken(updatePassword.getToken());
        if(!isValidateSuccess){
            mqttClient.publish(Topic.changePasswordTopic+"/"+updatePassword.getRealDeviceId(),setPayload(new Result<>(null,ErrorMessage.tokenExpiration,403)));
        }
        else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Users users=(Users)authentication.getPrincipal();
            Result<?>result=usersService.editPassword(updatePassword.getPassword(), updatePassword.getNewPassword(), users.getUsername());
            mqttClient.publish(Topic.changePasswordTopic+"/"+updatePassword.getRealDeviceId(),setPayload(result));
        }
    }
    // Nghe và update user information     : Đã sửa
    public void listenAndPublishEditUserInfo(String json) throws JsonProcessingException, MqttException {
        UpdateUserInfo updateUserInfo=objectMapper.readValue(json,UpdateUserInfo.class);
        boolean isValidateSuccess= jwtAuthenticationFilter.handleToken(updateUserInfo.getToken());
        if(!isValidateSuccess){
            mqttClient.publish(Topic.editUserTopic+"/"+updateUserInfo.getRealDeviceId(),setPayload(new Result<>(null,ErrorMessage.tokenExpiration,403)));
        }
        else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Users users=(Users)authentication.getPrincipal();
                Result<?> result=usersService.editUserInfo(updateUserInfo.getPhone(),
                        updateUserInfo.getAddress(),
                        updateUserInfo.getFullName(),
                        updateUserInfo.getImageUrl(),
                        updateUserInfo.getEmail(),
                        users.getUsername());
                mqttClient.publish(Topic.editUserTopic+"/"+updateUserInfo.getRealDeviceId(),setPayload(result));
        }
    }
    // Nghe và gửi thiết bị theo user
    public void listenAndPublishDevice(String json) throws JsonProcessingException, MqttException {
        DeviceRequest deviceRequest=objectMapper.readValue(json,DeviceRequest.class);
        Result<List<DeviceResponse>> deviceResponse=deviceService.findUserDeviceByUserId(deviceRequest.getUserId());
        mqttClient.publish(Topic.deviceTopic+"/"+deviceRequest.getDeviceId(),setPayload(deviceResponse));
    }
    // Nghe và gửi lịch sử cảnh báo
    private void listenAndPublishAlert(String json) throws JsonProcessingException, MqttException {
        AlertRequest alertRequest=objectMapper.readValue(json, AlertRequest.class);
        boolean isValidateSuccess=jwtAuthenticationFilter.handleToken(alertRequest.getToken());
        if(!isValidateSuccess){
            mqttClient.publish(Topic.historyTopic+"/"+alertRequest.getRealDeviceId(),setPayload(new Result<>(null,ErrorMessage.tokenExpiration,403)));
        }else {
           Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Users users=(Users)authentication.getPrincipal();
                Result<List<AlertResponse>> result=alertService.getAlertsByTimeAndType(alertRequest,users.getUsername());
                mqttClient.publish(Topic.historyTopic+"/"+alertRequest.getRealDeviceId(),setPayload(result));
        }
    }
    // Nghe và tạo tài khoản người dùng
    private void listenAndPublishCreateUser(String json) throws JsonProcessingException, MqttException {
        CreateUserRequest createUserRequest=objectMapper.readValue(json,CreateUserRequest.class);
        final  String createTopicResponse=Topic.createUserTopic + "/"+createUserRequest.getRealDeviceId();
        boolean isValidateSuccess= jwtAuthenticationFilter.handleToken(createUserRequest.getToken());
        if (!isValidateSuccess){
            mqttClient.publish(createTopicResponse,setPayload(new Result<>(null,ErrorMessage.tokenExpiration,403)));
        }else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Users users=(Users)authentication.getPrincipal();
            System.out.println(users.getRoles().getRole().equals("ROLE_TECHNICAL"));
            if(users.getRoles().getRole().equals("ROLE_TECHNICAL")){
                Result<?>result = usersService.createUser(createUserRequest.getUsername(), createUserRequest.getPassword(), createUserRequest.getPhone());
                mqttClient.publish(createTopicResponse,setPayload(result));
            }else {
                mqttClient.publish(createTopicResponse,setPayload(new Result<>(null, ErrorMessage.unauthorized,403)));
            }
        }
    }
    // Nghe và gửi dữ liệu lượng điện sử dụng
    public void listenAndPublishPowerConsumption(String json) throws IOException, MqttException {
            DeviceLogPowerConsumptionRequest deviceLogPowerConsumptionRequest=objectMapper.readValue(json, DeviceLogPowerConsumptionRequest.class);
            String topicResponse=Topic.powerConsumption + "/" +deviceLogPowerConsumptionRequest.getRealDeviceId();
            boolean isValidateSuccess= jwtAuthenticationFilter.handleToken(deviceLogPowerConsumptionRequest.getToken());
            if(!isValidateSuccess){
                mqttClient.publish(topicResponse,setPayload(new Result<>(null,ErrorMessage.tokenExpiration,403)));
            }
            else {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                Users users=(Users)authentication.getPrincipal();
                for(UserDevices userDevices:users.getDevices()){
                    if(userDevices.getDevice().getDeviceName().contains(deviceLogPowerConsumptionRequest.getDeviceName())){
                        Result<Float> result= deviceLogService.powerConsumption(
                                deviceLogPowerConsumptionRequest.getStartDate(),
                                deviceLogPowerConsumptionRequest.getEndDate(),
                                deviceLogPowerConsumptionRequest.getDeviceName()
                        );
                        mqttClient.publish(Topic.powerConsumption + "/" +deviceLogPowerConsumptionRequest.getRealDeviceId(),setPayload(result));
                    }
                    else {
                        mqttClient.publish(Topic.powerConsumption + "/" +deviceLogPowerConsumptionRequest.getRealDeviceId(),setPayload(new Result<>(null,ErrorMessage.noAccess,403)));
                    }
                }
            }
    }
    // Nghe và lưu dữ liệu device log
    public  void listenAndSaveDeviceLog(String json){
        DeviceLog deviceLog = DeviceLogMapper.mapper(json);
        deviceLogService.save(deviceLog);
    }
    // Nghe và gửi dữ liệu login
    private void listenMessageLoginTopic(String json) throws JsonProcessingException, MqttException {
        AuthRequest authRequest = objectMapper.readValue(json,AuthRequest.class);
       try{
           Authentication authentication=authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
           UserDetails userDetails=(UserDetails)authentication.getPrincipal();
           String role=authentication.getAuthorities().iterator().next().getAuthority();
           Users users= usersService.findByUsername(userDetails.getUsername());
           String token = jwtUtil.generateToken(authRequest.getUsername(), role, users.getUsersId());
           Users user=usersService.findByUsername(userDetails.getUsername());
           UsersInfo usersInfo=user.getUsersInfo();
           AuthResponse authResponse=new AuthResponse(role,user.getUsername(),
                   usersInfo.getEmail(),
                   usersInfo.getImageUrl(),
                   usersInfo.getFullName(),
                   usersInfo.getAddress(),
                   user.getUsersId(),token);

           mqttClient.publish(Topic.loginTopic+"/"+ authRequest.getDeviceId(),setPayload(authResponse));}
       catch (Exception e) {
           mqttClient.publish(Topic.loginTopic+"/"+ authRequest.getDeviceId(),setPayload(new Result<>(null,"Thông tin tài khoảng hoặc mật khẩu không chính xác",401)));
       }
    }
    private void subscribeToTopics() {
        try {
            List<String> topics = new ArrayList<>(Topic.getAllTopics());
            for (String topic : topics) {
                mqttClient.subscribe(topic, 0);
                System.out.println("Subscribed to topic: " + topic);
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
