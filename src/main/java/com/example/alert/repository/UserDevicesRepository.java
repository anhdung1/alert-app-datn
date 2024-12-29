package com.example.alert.repository;

import com.example.alert.model.UserDevices;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface UserDevicesRepository extends JpaRepository<UserDevices,Integer> {
//    @Query("select ud from Users u join u.devices ud where u.usersId=:usersId")
//    List<UserDevices> findUserDeviceByUserId(@Param("usersId") Long usersId);
//    @Query("select d from UserDevices ud join ud.device d join ud.user u where u.usersId=:usersId")
//    List<UserDevices> findUserDeviceByUserId(@Param("usersId") Long usersId);
}
