package com.tmsjsb.redpanda.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmsjsb.redpanda.Entity.AppEntity;
import com.tmsjsb.redpanda.Service.AppService;
import com.tmsjsb.redpanda.Service.ErrorMgrService;

import jakarta.validation.Valid;

@RequestMapping("/app")
@RestController
public class AppController {
  
  private final AppService appService;

  public AppController(AppService appService) {
    this.appService = appService;
  }

  @GetMapping("/get")
  public ResponseEntity<?> getAllApp() {
    return ResponseEntity.ok().body(appService.getAllApps());
  }

  @PostMapping("/create")
  public ResponseEntity<Map<String, Object>> createApp(@RequestBody AppEntity app) {
    Map<String, Object> jsonObject = new HashMap<>();

    // App_Acronym validation (alphabets only)
    Pattern alphaOnlyPattern = Pattern.compile("[a-zA-Z]+");
    Matcher nameMatcher = alphaOnlyPattern.matcher(app.getApp_Acronym());
    Boolean validateName = nameMatcher.matches();

    // App_Rnumber validation (positive integers only)
    // when input integer into the API, error is not caught!!!
    Boolean validateRNumber = app.getApp_Rnumber() > 0;

    // App_Description validation (ASCII only)
    Pattern asciiPattern = Pattern.compile("\\A\\p{ASCII}*\\z");
    Matcher descriptionMatcher = asciiPattern.matcher(app.getApp_Description());
    Boolean validateDescription = descriptionMatcher.matches();

    // App_startDate and App_endDate checker ???

    if (validateName && validateRNumber && validateDescription) {
      jsonObject = appService.createApp(app.getApp_Acronym(), app.getApp_Description(), app.getApp_Rnumber(), app.getApp_startDate(), app.getApp_endDate(), app.getApp_permit_Create(), app.getApp_permit_Open(), app.getApp_permit_toDoList(), app.getApp_permit_Doing(), app.getApp_permit_Done());
    } else {
      jsonObject = ErrorMgrService.errorHandler("invalid fields", Thread.currentThread().getStackTrace()[1]);
    }

    return ResponseEntity.ok().body(jsonObject);
  }

  @PostMapping("/update")
  public ResponseEntity<Map<String, Object>> updateapp(@RequestBody AppEntity app) {
    Map<String, Object> jsonObject = new HashMap<>();

    // add validators
    //DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    //LocalDate startDate = LocalDate.parse(app.getApp_startDate(), dateformat);
    //LocalDate endDate = LocalDate.parse(app.getApp_endDate(), dateformat);

    Pattern asciiPattern = Pattern.compile("\\A\\p{ASCII}*\\z");
    Matcher descriptionMatcher = asciiPattern.matcher(app.getApp_Description());
    Boolean validateDescription = descriptionMatcher.matches();

    if (!validateDescription) {
      jsonObject = ErrorMgrService.errorHandler("invalid fields", Thread.currentThread().getStackTrace()[1]);
    } else {
    jsonObject = appService.updateApp(app.getApp_Acronym(), app.getApp_Description(), app.getApp_startDate(), app.getApp_endDate(), app.getApp_permit_Create(), app.getApp_permit_Open(), app.getApp_permit_toDoList(), app.getApp_permit_Doing(), app.getApp_permit_Done());
    }
    return ResponseEntity.ok().body(jsonObject);
  }
}
