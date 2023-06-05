package com.tmsjsb.redpanda.Controller.AdminController;

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
import com.tmsjsb.redpanda.Service.ErrorMgrService;
import com.tmsjsb.redpanda.Service.GroupsService;

import jakarta.validation.Valid;

@RequestMapping("/admin")
@RestController
public class AdminGroupController {

  private final GroupsService groupsService;

  public AdminGroupController(GroupsService groupsService) {
    this.groupsService = groupsService;
  }

  @PostMapping("/update/creategroup")
  public ResponseEntity<Map<String, Object>> createGroup(@Valid @RequestBody GroupsEntity group,
      BindingResult bindingResult) {

    Map<String, Object> jsonObject = new HashMap<>();

    // basic validations here
    if (bindingResult.hasErrors()) {
      // to add correct code
      jsonObject = ErrorMgrService.errorHandler("invalid fields", Thread.currentThread().getStackTrace()[1]);
      List<FieldError> errors = bindingResult.getFieldErrors();
      // jsonString = objectMapper.writeValueAsString(jsonObject);
      System.out.println(errors);
    } else {
      try {
        Map<String, Object> result = groupsService.createGroup(group);
        jsonObject = result;
      } catch (Exception e) {
        // if group already exists in db error (to add correct code)
        jsonObject = ErrorMgrService.errorHandler(e, Thread.currentThread().getStackTrace()[1]);
      }
    }

    return ResponseEntity.ok(jsonObject);
  }

  @PostMapping("/update/group")
  public ResponseEntity<Map<String, Object>> updateGroup(@RequestBody Map<String, Object> requestBody) {
    Map<String, Object> returnObject = new HashMap<>(0);
    String results = "true";

    try {
      String groupName = (String) requestBody.get("groupName");
      String username = (String) requestBody.get("username");
      String option = (String) requestBody.get("addOrRemove");

      GroupsEntity newGroup = new GroupsEntity();
      newGroup.setGroupName(groupName);
      newGroup.setUsername(username);
      if (option.equals("remove")) {
        groupsService.removeGroup(newGroup);
      } else {
        groupsService.createGroup(newGroup);
      }

    } catch (Exception e) {
      results = "internal error";
      System.out.println("hello!" + e);
    }

    returnObject.put("results", results);

    return ResponseEntity.ok().body(returnObject);
  }

}
