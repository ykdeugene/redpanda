package com.tmsjsb.redpanda.Controller;

import com.tmsjsb.redpanda.Service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserGetController {

  private final UserService userService;

  public UserGetController(UserService userService) {
    this.userService = userService;
  }  

  @GetMapping("/get/profile")
  public ResponseEntity<Map<String,Object>> getProfile(@RequestHeader(value = "authorization", required = false) String token)
  {
    Map<String, Object> returnObject = new HashMap<>(0);
    System.out.println("check token: " + token);

    if(token == null || !token.contains("Bearer"))
    {
      System.out.println("check result: " + token == null);
      String results = "BSJ-370";
      returnObject.put("results", results);
      return ResponseEntity.ok().body(returnObject);
    } else {
      token = token.split(" ")[1];
      System.out.println("check token after split: " + token);
    }
    
    return ResponseEntity.ok().body(userService.getProfile(token));
  }

}
