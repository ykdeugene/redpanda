package com.tmsjsb.redpanda.Service;

import com.tmsjsb.redpanda.Entity.UserEntity;

import java.util.Optional;
import java.util.List;
import java.util.Map;

import org.springframework.http.converter.json.MappingJacksonValue;

public interface UserService {
    MappingJacksonValue login(String username, String password);

    Map<String, Object> updatePwd(String username, String password);

    Map<String, Object> updateEmail(String username, String email);

    List<UserEntity> getAllUsers();

    public Optional<UserEntity> getUserById(String id);
}
