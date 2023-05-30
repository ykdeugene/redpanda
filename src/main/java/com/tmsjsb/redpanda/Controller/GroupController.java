package com.tmsjsb.redpanda.Controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.ValidationException;

import com.tmsjsb.redpanda.Service.GroupsService;
import com.tmsjsb.redpanda.Entity.GroupsEntity;

@RestController

public class GroupController {
  // handled externally through dependncy injection frameworks like spring
  private GroupsService groupsService;

  // explicitly declared as a constructor parameter so spring identifies
  // groupservice bean and wires into the constructor,
  // initializint groupservice field during object creation
  @Autowired
  public GroupController(GroupsService groupsService) {
    this.groupsService = groupsService;
  }

  @GetMapping("/adminget/allGroup")
  public ResponseEntity<List<GroupsEntity>> getAllGroup() {
    List<GroupsEntity> groupsAndUsers = groupsService.getAllGroups();
    return new ResponseEntity<>(groupsAndUsers, HttpStatus.OK);
  }

  @PostMapping("/adminupdate/creategroup")
  public ResponseEntity<Map<String, Object>> createGroup(@Valid @RequestBody GroupsEntity group,
      BindingResult bindingResult) {

    Map<String, Object> jsonObject = new HashMap<>();

    System.out.println("gn: " + group.getGroupName());
    System.out.println("un: " + group.getUsername());

    // basic validations here

    if (bindingResult.hasErrors()) {
      // to add correct code
      jsonObject.put("results", "BSJxxx (validation failed)");
      List<FieldError> errors = bindingResult.getFieldErrors();
      // jsonString = objectMapper.writeValueAsString(jsonObject);
      System.out.println(errors);

    } else {
      try {
        GroupsEntity newUserGroupEntry = new GroupsEntity();
        newUserGroupEntry.setGroupName(group.getGroupName());
        newUserGroupEntry.setUsername(group.getUsername());
        groupsService.createGroup(newUserGroupEntry);
        jsonObject.put("results", "true");
      } catch (ValidationException e) {
        // if group already exists in db error (to add correct code)
        jsonObject.put("results", "BSJxxx (validation pass but gn exists in db)");
      }
    }

    return ResponseEntity.ok(jsonObject);
  }

  @PostMapping("/adminupdate/group")
  public ResponseEntity<Map<String,Object>> updateGroup(@RequestBody Map<String,Object> requestBody)
  {
    Map<String, Object> returnObject = new HashMap<>(0);
    String results = "true";

    try{
      String groupName = (String) requestBody.get("groupName");
      String username = (String) requestBody.get("username");
      String option = (String) requestBody.get("option");

      GroupsEntity newGroup = new GroupsEntity();
      newGroup.setGroupName(groupName);
      newGroup.setUsername(username);
      if(option.equals("remove"))
      {
        groupsService.removeGroup(newGroup);
      }
      else
      {
        groupsService.createGroup(newGroup);
      }

    }catch(Exception e)
    {
      results = "internal error";
    }
    
    returnObject.put("results",results);

    return ResponseEntity.ok().body(returnObject);
  }

}