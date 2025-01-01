package com.example.alert.repository;

import com.example.alert.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
    boolean existsByUsername(String username);
    @Query("select u from Users u where u.username=:username and u.password=:password")
    Users findByUsernameAndPassword(@Param("username") String username,@Param("password") String password);
//      Users findByUsernameAndPassword(String username,String password);
    @Query("select u from Users u join u.devices ud join ud.device d where u.username = :username and d.deviceName = :deviceName")
    Users findByDeviceName(@Param("deviceName") String deviceName,@Param("username")String username);
}
