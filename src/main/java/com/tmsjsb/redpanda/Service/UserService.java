package com.tmsjsb.redpanda.Service;

import com.tmsjsb.redpanda.Entity.UserEntity;

import java.util.Optional;

import org.springframework.http.converter.json.MappingJacksonValue;



public interface UserService {
    MappingJacksonValue login(String username, String password);
    MappingJacksonValue updatePwd(String username, String password);
    MappingJacksonValue updateEmail(String username, String email);

    public Optional<UserEntity> getUserById(String id);
}
