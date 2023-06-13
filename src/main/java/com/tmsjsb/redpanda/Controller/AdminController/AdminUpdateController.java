package com.tmsjsb.redpanda.Controller.AdminController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmsjsb.redpanda.Entity.UserEntity;
import com.tmsjsb.redpanda.Interface.CompositeValidationGroup;
import com.tmsjsb.redpanda.Interface.UserOnUpdateEmail;
import com.tmsjsb.redpanda.Interface.UserOnUpdatePassword;
import com.tmsjsb.redpanda.Service.ErrorMgrService;
import com.tmsjsb.redpanda.Service.UserService;

import jakarta.validation.Valid;

@RequestMapping("/admin")
@RestController
public class AdminUpdateController {

  private final UserService userService;

  public AdminUpdateController(UserService userService) {
    this.userService = userService;
  }

  @Validated(UserOnUpdatePassword.class)
  @PostMapping("/update/pwd")
  public ResponseEntity<Map<String, Object>> adminUpdateUserPwd(@Valid @RequestBody Map<String, Object> requestBody,
      BindingResult bindingResult) {

    System.out.println("Size: " + requestBody.size());

    Map<String, Object> jsonObject = new HashMap<>();

    UserEntity user = new UserEntity();
    user.setUsername((String) requestBody.get("username"));
    user.setPassword((String) requestBody.get("password"));

    Pattern pattern = Pattern.compile(
        "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~])[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~]{8,10}$");
    Matcher matcher = pattern.matcher(user.getPassword());
    Boolean validatePwd = matcher.matches();

    if (bindingResult.hasErrors()) {
      // to add correct code
      jsonObject = ErrorMgrService.errorHandler("invalid fields", Thread.currentThread().getStackTrace()[1]);
      List<FieldError> errors = bindingResult.getFieldErrors();
      // jsonString = objectMapper.writeValueAsString(jsonObject);
      System.out.println("error in binding updatePwd: " + errors);
    } else if (!validatePwd) {
      jsonObject = ErrorMgrService.errorHandler("invalid fields", Thread.currentThread().getStackTrace()[1]);
    } else {
      try {
        Map<String, Object> result = userService.adminUpdateUserPwd(user.getUsername(), user.getPassword());
        jsonObject = result;
      } catch (Exception e) {
        jsonObject = ErrorMgrService.errorHandler(e, Thread.currentThread().getStackTrace()[1]);
      }
    }
    return ResponseEntity.ok(jsonObject);
  }

  @Validated(UserOnUpdateEmail.class)
  @PostMapping("/update/email")
  public ResponseEntity<Map<String, Object>> adminUpdateUserEmail(@Valid @RequestBody UserEntity user,
      BindingResult bindingResult) {

    Map<String, Object> jsonObject = new HashMap<>();

    if (bindingResult.hasErrors()) {
      // to add correct code
      List<FieldError> errors = bindingResult.getFieldErrors();
      // jsonString = objectMapper.writeValueAsString(jsonObject);
      System.out.println(errors);
      jsonObject = ErrorMgrService.errorHandler("invalid fields", Thread.currentThread().getStackTrace()[1]);
    } else {
      try {
        Map<String, Object> result = userService.adminUpdateUserEmail(user.getUsername(), user.getEmail());
        jsonObject = result;
      } catch (Exception e) {
        jsonObject = ErrorMgrService.errorHandler(e, Thread.currentThread().getStackTrace()[1]);
      }
    }
    return ResponseEntity.ok(jsonObject);
  }

  @Validated(CompositeValidationGroup.class)
  @PostMapping("/update/createuser")
  public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody UserEntity user, BindingResult bindingResult) {
    Map<String, Object> returnObject = new HashMap<>(0);

    Pattern pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~])[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~]{8,10}$");
    Matcher matcher = pattern.matcher(user.getPassword());
    Boolean validatePwd = matcher.matches();

    if (bindingResult.hasErrors()) {
      returnObject = ErrorMgrService.errorHandler("invalid fields", Thread.currentThread().getStackTrace()[1]);
      List<FieldError> errors = bindingResult.getFieldErrors();
      // jsonString = objectMapper.writeValueAsString(jsonObject);
      System.out.println(errors);
      return ResponseEntity.ok().body(returnObject);
    } else if (!validatePwd) {
      returnObject = ErrorMgrService.errorHandler("invalid credentials", Thread.currentThread().getStackTrace()[1]);
    }
    return ResponseEntity.ok().body(userService.createUser(user.getUsername(), user.getPassword(), user.getEmail(), user.getActiveStatus()));
  }

  @PostMapping("/update/activestatus")
  public ResponseEntity<Map<String, Object>> updateActiveStatus(@RequestBody Map<String, Object> requestBody) {
    String username = (String) requestBody.get("username");
    int activeStatus = (int) requestBody.get("activeStatus");
    return ResponseEntity.ok().body(userService.updateActiveStatus(username, activeStatus));
  }

}
