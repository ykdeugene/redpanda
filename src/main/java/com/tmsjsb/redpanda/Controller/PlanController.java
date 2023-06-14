package com.tmsjsb.redpanda.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tmsjsb.redpanda.Entity.PlanEntity;
import com.tmsjsb.redpanda.Service.ErrorMgrService;
import com.tmsjsb.redpanda.Service.PlanService;

@RestController
public class PlanController {

  private final PlanService planService;

  @Autowired
  public PlanController(PlanService planService) {
    this.planService = planService;
  }

  @PostMapping("/plan/get")
  public ResponseEntity<?> getPlanByPlanAppAcronym(@RequestBody PlanEntity plan) {

    Map<String, Object> jsonObject = new HashMap<>();

    try {
      List<Map<String, Object>> plans = planService.getPlan(plan.getPlanappAcronym());
      return ResponseEntity.ok().body(plans);
    } catch (Exception e) {
      jsonObject = ErrorMgrService.errorHandler(e, Thread.currentThread().getStackTrace()[1]);
    }
    return ResponseEntity.ok(jsonObject);
  }

  @PostMapping("/plan/create")
  public ResponseEntity<?> createNewPlan(@RequestBody PlanEntity plan) {

    Map<String, Object> jsonObject = new HashMap<>();

    // System.out.println(plan.getPlanMVPname());
    // System.out.println(plan.getPlanappAcronym());
    // System.out.println(plan.getPlanstartDate());
    // System.out.println(plan.getPlanendDate());
    // System.out.println(plan.getPlancolour());

    // if empty fields, throw error
    if (plan.getPlanMVPname() == null || plan.getPlanappAcronym() == null
        || plan.getPlanstartDate() == null
        || plan.getPlanendDate() == null || plan.getPlancolour() == null) {
      jsonObject = ErrorMgrService.errorHandler("invalid fields", Thread.currentThread().getStackTrace()[1]);
    } else if (plan.getPlanMVPname().isBlank() || plan.getPlanappAcronym().isBlank()
        || plan.getPlanstartDate().isBlank()
        || plan.getPlanendDate().isBlank() || plan.getPlancolour().isBlank()) {
      jsonObject = ErrorMgrService.errorHandler("invalid fields", Thread.currentThread().getStackTrace()[1]);
    } //
    // else if checkgroup here. if pass then run else below.
    else {
      // else run service to save into db.
      try {
        Map<String, Object> result = planService.createPlan(plan);
        jsonObject = result;
      } catch (Exception e) {
        jsonObject = ErrorMgrService.errorHandler(e, Thread.currentThread().getStackTrace()[1]);
      }
    }

    return ResponseEntity.ok(jsonObject);
  }

  @PostMapping("/plan/update")
  public ResponseEntity<?> updatePlan(@RequestBody PlanEntity plan) {

    Map<String, Object> jsonObject = new HashMap<>();

    // if empty fields, throw error
    if (plan.getPlanMVPname() == null || plan.getPlanappAcronym() == null
        || plan.getPlanstartDate() == null
        || plan.getPlanendDate() == null || plan.getPlancolour() == null) {
      jsonObject = ErrorMgrService.errorHandler("invalid fields", Thread.currentThread().getStackTrace()[1]);
    } else if (plan.getPlanMVPname().isBlank() || plan.getPlanappAcronym().isBlank()
        || plan.getPlanstartDate().isBlank()
        || plan.getPlanendDate().isBlank() || plan.getPlancolour().isBlank()) {
      jsonObject = ErrorMgrService.errorHandler("invalid fields", Thread.currentThread().getStackTrace()[1]);
    } //
    // else if checkgroup here. if pass then run else below.
    else {
      // else run service to save into db.
      try {
        Map<String, Object> result = planService.updatePlan(plan);
        jsonObject = result;
      } catch (Exception e) {
        jsonObject = ErrorMgrService.errorHandler(e, Thread.currentThread().getStackTrace()[1]);
      }
    }

    return ResponseEntity.ok(jsonObject);
  }

}
