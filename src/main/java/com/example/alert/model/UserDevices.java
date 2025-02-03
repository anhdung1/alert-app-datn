package com.example.alert.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.sql.results.graph.Fetch;

@Entity
public class UserDevices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userDevicesId;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "device_id")
    private Device device;



    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public int getUserDevicesId() {
        return userDevicesId;
    }

    public void setUserDevicesId(int userDevicesId) {
        this.userDevicesId = userDevicesId;
    }
}
