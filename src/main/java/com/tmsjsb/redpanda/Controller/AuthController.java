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

import com.tmsjsb.redpanda.Entity.GroupsEntity;
import com.tmsjsb.redpanda.Entity.UserEntity;
import com.tmsjsb.redpanda.Service.AuthService;
import com.tmsjsb.redpanda.Service.ErrorMgrService;
import com.tmsjsb.redpanda.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RequestMapping("/auth")
// @CrossOrigin(origins = "http://localhost:3000")
@RestController
public class AuthController {
  
  private final UserService userService;
  private final AuthService authservice;

  public AuthController(UserService userService, AuthService authservice) {
    this.userService = userService;
    this.authservice = authservice;
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, Object>> AuthLogin(HttpServletRequest request, @Valid @RequestBody UserEntity user,
      BindingResult bindingResult) {
    Map<String, Object> jsonObject = new HashMap<>();
    
    String ipAddress = request.getRemoteAddr();
    String browserType = request.getHeader("User-Agent");

    if (bindingResult.hasErrors()) {
      // to add correct code
      jsonObject = ErrorMgrService.errorHandler("invalid parameters", Thread.currentThread().getStackTrace()[1]);
      List<FieldError> errors = bindingResult.getFieldErrors();
      System.out.println(errors);
    } else {
      try {
        Map<String, Object> result = userService.login(user.getUsername(), user.getPassword(), ipAddress, browserType);
        jsonObject = result;
      } catch (Exception e) {
        jsonObject = ErrorMgrService.errorHandler(e, Thread.currentThread().getStackTrace()[1]);
      }
    }
    return ResponseEntity.ok(jsonObject);
  }

  @PostMapping("/checkgroup")
  public ResponseEntity<Map<String, Object>> Authcheckgroup(HttpServletRequest request, @RequestBody GroupsEntity usergroup) {
    Map<String, Object> jsonObject = new HashMap<>();

    String jwt = request.getHeader("Authorization");
    String username = authservice.TokenToUsername(jwt);
    try{
      boolean isgroup = authservice.CheckGroup(username, usergroup.getGroupName());
      jsonObject.put("result", isgroup);
    } catch(Exception e){
      jsonObject = ErrorMgrService.errorHandler(e, Thread.currentThread().getStackTrace()[1]);
    }
    
    return ResponseEntity.ok(jsonObject);
  }
}
