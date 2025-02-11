package com.example.alert.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    
    public void sendNotification(String token, String title, String body) throws FirebaseMessagingException {
        try{
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(notification)
                    .build();
            String response = FirebaseMessaging.getInstance().send(message);
        }
        catch (FirebaseMessagingException e){
            if ("registration-token-not-registered".equals(e.getMessagingErrorCode().toString())) {

            }
        }

    }
}
