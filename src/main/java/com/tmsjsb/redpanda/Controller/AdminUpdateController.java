package com.tmsjsb.redpanda.Controller;

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
import com.tmsjsb.redpanda.Interface.UserOnCreate;
import com.tmsjsb.redpanda.Interface.UserOnUpdateEmail;
import com.tmsjsb.redpanda.Interface.UserOnUpdatePassword;
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
  public ResponseEntity<Map<String, Object>> adminUpdateUserPwd(@Valid @RequestBody UserEntity user,
      BindingResult bindingResult) {

    Map<String, Object> jsonObject = new HashMap<>();

    Pattern pattern = Pattern.compile(
    "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~])[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~]{8,10}$");
    Matcher matcher = pattern.matcher(user.getPassword());
    Boolean validatePwd = matcher.matches();

    if (bindingResult.hasErrors()) {
      // to add correct code
      jsonObject.put("results", "BSJxxx (validation failed)");
      List<FieldError> errors = bindingResult.getFieldErrors();
      // jsonString = objectMapper.writeValueAsString(jsonObject);
      System.out.println(errors);
    } else if (!validatePwd) {
      jsonObject.put("results", "BSJxxx (password validation failed)");
    } else {
      try {
        Map<String, Object> result = userService.updatePwd(user.getUsername(), user.getPassword());
        jsonObject = result;
      } catch (Exception e) {
        jsonObject.put("results", "BSJxxx (exception error)");
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
      jsonObject.put("results", "BSJxxx (validation failed)");
      List<FieldError> errors = bindingResult.getFieldErrors();
      // jsonString = objectMapper.writeValueAsString(jsonObject);
      System.out.println(errors);
    } else {
      try {
        Map<String, Object> result = userService.updateEmail(user.getUsername(), user.getEmail());
        jsonObject = result;
      } catch (Exception e) {
        jsonObject.put("results", "BSJxxx (exception error)");
      }
    }
    return ResponseEntity.ok(jsonObject);
  }

  @Validated(UserOnCreate.class)
  @PostMapping("/update/createuser")
  public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody UserEntity user, BindingResult bindingResult)
  {
    Map<String, Object> returnObject = new HashMap<>(0);

    Pattern pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~])[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~]{8,10}$");
    Matcher matcher = pattern.matcher(user.getPassword());
    Boolean validatePwd = matcher.matches();

    if(bindingResult.hasErrors())
    {
      returnObject.put("results", "BSJxxx (validation failed)");
      return ResponseEntity.ok().body(returnObject);
    } else if (!validatePwd) {
      returnObject.put("results", "BSJ-369b (password validation failed)");
    }
    return ResponseEntity.ok().body(userService.createUser(user.getUsername(),user.getPassword(),user.getEmail(),user.getActiveStatus()));
  }

}
