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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.tmsjsb.redpanda.Entity.UserEntity;
import com.tmsjsb.redpanda.Interface.UserOnUpdateEmail;
import com.tmsjsb.redpanda.Interface.UserOnUpdatePassword;
import com.tmsjsb.redpanda.Service.ErrorMgrService;
import com.tmsjsb.redpanda.Service.UserService;

import jakarta.validation.Valid;

@RestController
public class UserUpdateController {
  
  private final UserService userService;

  public UserUpdateController(UserService userService) {
    this.userService = userService;
  }

  @Validated(UserOnUpdateEmail.class)
  @PostMapping("/update/email")
  public ResponseEntity<Map<String, Object>> UpdateUserEmail(@Valid @RequestHeader(value = "authorization", required = false) String token, @RequestBody UserEntity user, BindingResult bindingResult) {
    Map<String, Object> jsonObject = new HashMap<>();

    if (bindingResult.hasErrors() || !token.contains("Bearer ")) {
      // to add correct code
      jsonObject.put("result", "BSJxxx (validation failed)");
      List<FieldError> errors = bindingResult.getFieldErrors();
      System.out.println(errors);
    } else {
      try {
        token = token.split(" ")[1];
        Map<String, Object> result = userService.updateEmail(token, user.getEmail());
        jsonObject = result;
      } catch (Exception e) {
        jsonObject = ErrorMgrService.errorHandler(e, Thread.currentThread().getStackTrace()[1]);
      }
    }
    return ResponseEntity.ok(jsonObject);
  }

  @Validated(UserOnUpdatePassword.class)
  @PostMapping("/update/pwd")
  public ResponseEntity<Map<String, Object>> UpdateUserPwd(@Valid @RequestHeader(value = "authorization", required = false) String token, @RequestBody UserEntity user, BindingResult bindingResult) {

    Map<String, Object> jsonObject = new HashMap<>();

    Pattern pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~])[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?`~]{8,10}$");
    Matcher matcher = pattern.matcher(user.getPassword());
    Boolean validatePwd = matcher.matches();

    if (bindingResult.hasErrors() || !token.contains("Bearer")) {
      // to add correct code
      jsonObject.put("result", "BSJxxx (bindingResult password validation failed)");
      List<FieldError> errors = bindingResult.getFieldErrors();
      // jsonString = objectMapper.writeValueAsString(jsonObject);
      System.out.println(errors);
    } else if (!validatePwd) {
      jsonObject.put("result", "BSJxxx (password validation failed)");
    } else {
      token = token.split(" ")[1];
      try {
        Map<String, Object> result = userService.updatePwd(token, user.getPassword());
        jsonObject = result;
      } catch (Exception e) {
        jsonObject = ErrorMgrService.errorHandler(e, Thread.currentThread().getStackTrace()[1]);
      }
    }
    return ResponseEntity.ok(jsonObject);
  }
}
