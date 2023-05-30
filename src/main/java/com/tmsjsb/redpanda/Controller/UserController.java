package com.tmsjsb.redpanda.Controller;

import com.tmsjsb.redpanda.Service.UserService;
import com.tmsjsb.redpanda.Entity.UserEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.converter.json.MappingJacksonValue;
// import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

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

  @RequestMapping("/adminget/allprofile")
  public ResponseEntity<List<UserEntity>> getAllUsers() {
    List<UserEntity> users = userService.getAllUsers();
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @PostMapping("/adminupdate/pwd")
  public ResponseEntity<Map<String, Object>> adminUpdateUserPwd(@Valid @RequestBody UserEntity user,
      BindingResult bindingResult) {

    Map<String, Object> jsonObject = new HashMap<>();

    if (bindingResult.hasErrors()) {
      // to add correct code
      jsonObject.put("results", "BSJxxx (validation failed)");
      List<FieldError> errors = bindingResult.getFieldErrors();
      // jsonString = objectMapper.writeValueAsString(jsonObject);
      System.out.println(errors);
    } else {
      try {
        userService.updatePwd(user.getUsername(), user.getPassword());
        jsonObject.put("results", "true");
      } catch (Exception e) {
        // if
        jsonObject.put("results", "BSJxxx (exception error)");
      }
    }
    return ResponseEntity.ok(jsonObject);
  }

  @PostMapping("/adminupdate/email")
  public ResponseEntity<Map<String, Object>> adminUpdateUserEmail(@Valid @RequestBody UserEntity user,
      BindingResult bindingResult) {

    Map<String, Object> jsonObject = new HashMap<>();

    if (bindingResult.hasErrors()) {
      // to add correct code
      jsonObject.put("results", "BSJxxx (validation failed)");
      List<FieldError> errors = bindingResult.getFieldErrors();
      // jsonString = objectMapper.writeValueAsString(jsonObject);
      System.out.println(errors);
    } else {
      try {
        userService.updatePwd(user.getUsername(), user.getEmail());

        jsonObject.put("results", "true");
      } catch (Exception e) {
        // if
        jsonObject.put("results", "BSJxxx (exception error)");
      }
    }
    return ResponseEntity.ok(jsonObject);

  }

}
