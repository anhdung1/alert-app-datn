package com.example.alert.repository;

import com.example.alert.dtos.DeviceResponse;
import com.example.alert.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device,Integer> {
    @Query("SELECT new com.example.alert.dtos.DeviceResponse(d.deviceId, d.model, d.version) " +
            "FROM UserDevices ud " +
            "JOIN ud.device d " +
            "JOIN ud.user u " +
            "WHERE u.usersId = :usersId")
    List<DeviceResponse> findUserDeviceByUserId(@Param("usersId") Long usersId);
}
