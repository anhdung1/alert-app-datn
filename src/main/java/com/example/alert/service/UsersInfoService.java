package com.example.alert.service;

import com.example.alert.model.Users;
import com.example.alert.model.UsersInfo;
import com.example.alert.repository.UsersInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersInfoService {
    @Autowired
    private UsersInfoRepository usersInfoRepository;

    public UsersInfoRepository getUsersInfoRepository() {
        return usersInfoRepository;
    }
    public void saveUsersInfo(Users users,String phone) {
        UsersInfo usersInfo=new UsersInfo();
        usersInfo.setUser(users);
        usersInfo.setPhone(phone);
        usersInfoRepository.save(usersInfo);
    }

}
