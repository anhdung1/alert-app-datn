package com.example.alert.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rolesId;
    private String role;
    @JsonIgnore
    @OneToMany(mappedBy = "roles")
    private List<Users> users;
    public int getRoleId() {
        return rolesId;
    }

    public void setRoleId(int rolesId) {
        this.rolesId = rolesId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
