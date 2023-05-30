package com.tmsjsb.redpanda.Controller;

import com.tmsjsb.redpanda.Service.UserService;
import com.tmsjsb.redpanda.Entity.UserEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.converter.json.MappingJacksonValue;
// import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class UserController {

  private final UserService userService;

    private UserController(UserService userService) {
        this.userService = userService;
    }

  @PostMapping("/auth/login")
  public MappingJacksonValue Authlogin(@RequestBody UserEntity user) {
    return userService.login(user.getUsername(), user.getPassword());
  }

  @PostMapping("/update/pwd")
  public MappingJacksonValue updatePwd(@RequestBody UserEntity user) {
    return userService.updatePwd(user.getUsername(), user.getPassword());
  }

  @PostMapping("/update/email")
  public MappingJacksonValue updateEmail(@RequestBody UserEntity user) {
    return userService.updateEmail(user.getUsername(), user.getEmail());
  }
  
} 
