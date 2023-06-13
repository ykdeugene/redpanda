package com.tmsjsb.redpanda.Controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmsjsb.redpanda.Entity.AppEntity;
import com.tmsjsb.redpanda.Service.AppService;

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
  public ResponseEntity<Map<String, Object>> createUser(@RequestBody AppEntity app) {
    return ResponseEntity.ok().body(appService.createApp(app.getApp_Acronym(), app.getApp_Description(), app.getApp_Rnumber(), app.getApp_startDate(), app.getApp_endDate(), app.getApp_permit_Create(), app.getApp_permit_Open(), app.getApp_permit_toDoList(), app.getApp_permit_Doing(), app.getApp_permit_Done()));
  }
}
