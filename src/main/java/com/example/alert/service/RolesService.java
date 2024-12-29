package com.example.alert.service;

import com.example.alert.model.Roles;
import com.example.alert.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolesService {
    @Autowired
    private RolesRepository rolesRepository;

    public RolesRepository getRolesRepository() {
        return rolesRepository;
    }
}
