package com.example.alert.repository;

import com.example.alert.model.FirebaseTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FirebaseTokensRepository extends JpaRepository<FirebaseTokens,Integer> {
    FirebaseTokens findByToken(String token);
    @Query("Select f.token from FirebaseTokens f join f.user u join u.devices ud join ud.device d where d.deviceName =:deviceName")
    List<String> findFirebaseTokenByDeviceName(@Param("deviceName") String deviceName);
}
