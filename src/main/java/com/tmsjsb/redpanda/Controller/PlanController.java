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

}
