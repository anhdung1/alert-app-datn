package com.example.alert.service;

import com.example.alert.model.Roles;
import com.example.alert.model.Users;
import com.example.alert.repository.RolesRepository;
import com.example.alert.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private  UsersRepository usersRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private RolesService rolesService;
    @Autowired
    private UsersInfoService usersInfoService;
    public UsersRepository getUsersRepository() {
        return usersRepository;
    }
    @Cacheable(value = "users", key = "#username")
    public Users findByUsername(String username){
        return usersRepository.findByUsername(username);
    }
    public String encodePassword(String password){
       return passwordEncoder.encode(password);
    }
    public Result<String> createUser(String username, String password,String phone){
        boolean isExists= usersRepository.existsByUsername(username);
        if(isExists){
            return new Result<>(null,"Username đã tồn tại",404);
        }
        Users user = new Users();
        user.setUsername(username);
        Roles role = rolesService.getRolesRepository().findByRole("ROLE_USER");
        user.setRoles(role);
        user.setPassword(encodePassword(password));
        usersRepository.save(user);
        usersInfoService.saveUsersInfo(user,phone);
        return new Result<>(null,"Success",200);
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public void createSubUser(String username, String password,Long userId,String phone){
        Users user = new Users();
        user.setUsername(username);
        Roles role = rolesService.getRolesRepository().findByRole("ROLE_SUB_USER");
        user.setRoles(role);
        user.setCreatedBy(userId);
        user.setPassword(encodePassword(password));
        usersRepository.save(user);
        usersInfoService.saveUsersInfo(user,phone);
    }

}
