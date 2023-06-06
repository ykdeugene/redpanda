package com.tmsjsb.redpanda.Controller.AdminController;

import com.tmsjsb.redpanda.Service.GroupsService;
import com.tmsjsb.redpanda.Service.UserService;
import com.tmsjsb.redpanda.Entity.GroupsEntity;
import com.tmsjsb.redpanda.Entity.UserEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@RequestMapping(("/admin"))
@RestController
public class AdminGetController {

  private final UserService userService;
  private final GroupsService groupsService;

  public AdminGetController(UserService userService, GroupsService groupService) {
    this.userService = userService;
    this.groupsService = groupService;
  }

  @GetMapping("/get/allprofile")
  public ResponseEntity<?> getAllUsers() {
    //List<UserEntity> users = userService.getAllUsers();
    return ResponseEntity.ok().body(userService.getAllUsers());
  }

  @GetMapping("/get/allgroup")
  public ResponseEntity<List<GroupsEntity>> getAllGroup() {
    List<GroupsEntity> groupsAndUsers = groupsService.getAllGroups();
    return new ResponseEntity<>(groupsAndUsers, HttpStatus.OK);
  }
}
