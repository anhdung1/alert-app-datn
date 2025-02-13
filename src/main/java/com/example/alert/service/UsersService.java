package com.example.alert.service;

import com.example.alert.consts.ErrorMessage;
import com.example.alert.dtos.UsersResponse;
import com.example.alert.model.Roles;
import com.example.alert.model.Users;
import com.example.alert.model.UsersInfo;
import com.example.alert.repository.RolesRepository;
import com.example.alert.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
    @Transactional
    public Result<?> createUser(String username, String password,String phone){
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
    public Result<?> editUserInfo(String phone, String address, String fullName, String imageUrl, String email,String username){
        Users users=getUsersRepository().findByUsername(username);
        if(users!=null){
            UsersInfo usersInfoUpdate=users.getUsersInfo();
            usersInfoUpdate.setPhone(phone  );
            usersInfoUpdate.setAddress(address);
            usersInfoUpdate.setEmail(email);
            usersInfoUpdate.setFullName(fullName);
            usersInfoUpdate.setImageUrl(imageUrl);
            usersInfoService.getUsersInfoRepository().save(usersInfoUpdate);
            return new Result<>(null,"Success",200);
        }
        return new Result<>(null,ErrorMessage.usernameIncorrect,404);
    }
    public Result<?>editPassword(String password,String newPassword,String username){
        Users users=usersRepository.findByUsername(username);
        if(users==null)
            return new Result<>(null, ErrorMessage.passwordIncorrect,401);
        String encodedPassword=users.getPassword();
        boolean isPasswordMatch = passwordEncoder.matches(password, encodedPassword);
        if(!isPasswordMatch)
            return new Result<>(null, ErrorMessage.passwordIncorrect,401);
        users.setPassword(passwordEncoder.encode(newPassword));
        usersRepository.save(users);
        return new Result<>(null,ErrorMessage.success,200);
    }
    @Transactional
    public void deleteUserByUserId(Long userId){
        Optional<Users>user =usersRepository.findById(userId);
        usersRepository.deleteById(userId);
    }
    public Result<List<UsersResponse>> getAllUsers(){
        List<Users> userList = usersRepository.findAll();
        return new Result<>(convertToUsersResponse(userList), ErrorMessage.success,200);
    }
    public List<UsersResponse> convertToUsersResponse(List<Users> users){
        List<UsersResponse> listUsersResponses = new ArrayList<>();
        for(Users user:users){
            UsersResponse usersResponse = new UsersResponse(user.getUsername(),user.getUsersId(),user.getRoles().getRole(),user.getUsersInfo());
            listUsersResponses.add(usersResponse);
        }
        return listUsersResponses;
    }
}
