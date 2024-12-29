package com.example.alert.service;

import com.example.alert.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    public DeviceRepository getDeviceRepository() {
        return deviceRepository;
    }
}
