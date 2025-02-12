package com.example.alert.service;

import com.example.alert.repository.FirebaseTokensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirebaseTokensService {
    @Autowired
    private FirebaseTokensRepository firebaseTokensRepository;

    public FirebaseTokensRepository getFirebaseTokensRepository() {
        return firebaseTokensRepository;
    }
}
