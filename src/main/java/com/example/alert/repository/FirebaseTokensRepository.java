package com.example.alert.repository;

import com.example.alert.model.FirebaseTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface FirebaseTokensRepository extends JpaRepository<FirebaseTokens,Integer> {
    FirebaseTokens findByToken(String token);
}
