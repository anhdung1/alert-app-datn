package com.example.alert.model;

import jakarta.persistence.*;
@Entity
@Table(name = "firebase_tokens")
public class FirebaseTokens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int firebaseTokensId;
    private String token;
    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users user;

    public int getFirebaseTokensId() {
        return firebaseTokensId;
    }

    public void setFirebaseTokensId(int firebaseTokensId) {
        this.firebaseTokensId = firebaseTokensId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
