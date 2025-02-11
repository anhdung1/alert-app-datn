package com.example.alert.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    private Long usersId;
    private String username;
    private String password;
    private Long createdBy;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Roles roles;
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "user")
    private List<UserDevices> devices;
    public Long getUsersId() {
        return usersId;
    }
    @OneToOne(mappedBy = "user",fetch = FetchType.EAGER)
    private UsersInfo usersInfo;
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "user")
    private List<FirebaseTokens> firebaseTokensList;

    public void setUsersId(Long usersId) {
        this.usersId = usersId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public UsersInfo getUsersInfo() {
        return usersInfo;
    }

    public void setUsersInfo(UsersInfo usersInfo) {
        this.usersInfo = usersInfo;
    }


    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public List<UserDevices> getDevices() {
        return devices;
    }

    public void setDevices(List<UserDevices> devices) {
        this.devices = devices;
    }
}
