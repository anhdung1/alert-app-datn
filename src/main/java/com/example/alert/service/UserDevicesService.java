package com.example.alert.service;

import com.example.alert.model.Device;
import com.example.alert.model.UserDevices;
import com.example.alert.model.Users;
import com.example.alert.repository.UserDevicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDevicesService {
    @Autowired
    private  DeviceService deviceService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private UserDevicesRepository userDevicesRepository;

    public UserDevicesRepository getUserDevicesRepository() {
        return userDevicesRepository;
    }
    public Result<String> saveUserDevices(Long userId,String role,Integer deviceId ){
               if(role.equals("ROLE_TECHNICAL")){
                   Optional<Users> users= usersService.getUsersRepository().findById(userId);
                   Optional<Device> device=deviceService.getDeviceRepository().findById(deviceId);
                   if(users.isPresent() &&device.isPresent()){
                       UserDevices userDevices=new UserDevices();
                       userDevices.setDevice(device.get());
                       userDevicesRepository.save(userDevices);
                       return new Result<>(null,"",200);
                   }
                   return new Result<>(null,"User hoặc thiêt bị không tồn tại",404);
               }
        return new Result<>(null,"User hoặc thiêt bị không tồn tại",403);
    }
}
