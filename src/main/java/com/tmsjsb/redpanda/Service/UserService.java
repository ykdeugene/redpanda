package com.tmsjsb.redpanda.Service;

import com.tmsjsb.redpanda.Entity.UserEntity;

import java.util.Map;
import java.util.Optional;
import java.util.List;

public interface UserService {
    Map<String, Object> login(String username, String password);

    Map<String, Object> updatePwd(String username, String password);

    Map<String, Object> updateEmail(String username, String email);

    List<UserEntity> getAllUsers();
    Map<String,Object> createUser(String username, String password, String email,int activeStatus);
    Map<String,Object> getProfile(String token);

    public Optional<UserEntity> getUserById(String id);
}
