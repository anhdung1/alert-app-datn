package com.example.alert.service;

import com.example.alert.dtos.DeviceResponse;
import com.example.alert.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    public DeviceRepository getDeviceRepository() {
        return deviceRepository;
    }
    public Result<List<DeviceResponse>> findUserDeviceByUserId(Long usersId){
        return new Result<List<DeviceResponse>>(deviceRepository.findUserDeviceByUserId(usersId),"",200);
    }
}
