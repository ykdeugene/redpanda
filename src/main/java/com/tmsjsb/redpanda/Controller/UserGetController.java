package com.tmsjsb.redpanda.Controller;

import com.tmsjsb.redpanda.Service.UserService;
import com.tmsjsb.redpanda.Service.GroupsService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserGetController {

  private final GroupsService groupsService;

  private final UserService userService;

  public UserGetController(UserService userService, GroupsService groupsService) {
    this.userService = userService;
    this.groupsService = groupsService;
  }

  @GetMapping("/get/profile")
  public ResponseEntity<Map<String, Object>> getProfile(
      @RequestHeader(value = "authorization", required = false) String token) {
    Map<String, Object> returnObject = new HashMap<>(0);

    if (token == null || !token.contains("Bearer")) {
      String result = "BSJ-370";
      returnObject.put("result", result);
      return ResponseEntity.ok().body(returnObject);
    } else {
      token = token.split(" ")[1];
    }

    return ResponseEntity.ok().body(userService.getProfile(token));
  }

  @GetMapping("/get/group")
  public ResponseEntity<?> getGroup(@RequestHeader(value = "authorization", required = false) String token) {
    Map<String, Object> returnObject = new HashMap<>(0);

    if (token == null || !token.contains("Bearer")) {
      System.out.println("check result: " + token == null);
      String result = "BSJ-370";
      returnObject.put("result", result);
      return ResponseEntity.ok().body(returnObject);
    }

    token = token.split(" ")[1];
    System.out.println("check token after split: " + token);

    return ResponseEntity.ok().body(groupsService.getGroup(token));
  }

}
