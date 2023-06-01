package com.tmsjsb.redpanda.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmsjsb.redpanda.Entity.UserEntity;
import com.tmsjsb.redpanda.Service.UserService;

import jakarta.validation.Valid;

@RequestMapping("/auth")
@RestController
public class AuthController {
  
  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, Object>> AuthLogin(@Valid @RequestBody UserEntity user,
      BindingResult bindingResult) {
    Map<String, Object> jsonObject = new HashMap<>();

    if (bindingResult.hasErrors()) {
      // to add correct code
      jsonObject.put("results", "BSJxxx (validation failed)");
      List<FieldError> errors = bindingResult.getFieldErrors();
      System.out.println(errors);
    } else {
      try {
        Map<String, Object> result = userService.login(user.getUsername(), user.getPassword());
        jsonObject = result;
      } catch (Exception e) {
        jsonObject.put("results", "BSJxxx (exception error)");
      }
    }
    return ResponseEntity.ok(jsonObject);
  }
}
