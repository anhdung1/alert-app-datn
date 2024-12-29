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
    public void saveUserDevices(Long userId,Long subUserId,Integer deviceId ){
        Optional<Users> users= usersService.getUsersRepository().findById(userId);
        Optional<Users> subUsers= usersService.getUsersRepository().findById(subUserId);
        Optional<Device> device=deviceService.getDeviceRepository().findById(deviceId);
        if(users.isPresent() &&subUsers.isPresent()&&device.isPresent()){
            UserDevices userDevices=new UserDevices();
            userDevices.setDevice(device.get());
//            userDevices.set

        }

    }
}
