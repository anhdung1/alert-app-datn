package com.example.alert.dtos;

import com.example.alert.model.UsersInfo;

public class UsersResponse {
    private String username;
    private Long usersId;
    private String role;
    private UsersInfo usersInfo;
    public  UsersResponse(String username,Long usersId, String role, UsersInfo usersInfo){
        this.usersId=usersId;
        this.role=role;
        this.username=username;
        this.usersInfo=usersInfo;
    }
    public UsersInfo getUsersInfo() {
        return usersInfo;
    }

    public void setUsersInfo(UsersInfo usersInfo) {
        this.usersInfo = usersInfo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getUsersId() {
        return usersId;
    }

    public void setUsersId(Long usersId) {
        this.usersId = usersId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
