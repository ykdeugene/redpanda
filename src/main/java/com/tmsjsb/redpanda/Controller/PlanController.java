package com.tmsjsb.redpanda.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tmsjsb.redpanda.Entity.PlanEntity;
import com.tmsjsb.redpanda.Service.EmailService;
import com.tmsjsb.redpanda.Service.ErrorMgrService;
import com.tmsjsb.redpanda.Service.PlanService;

import jakarta.validation.Valid;

@RestController
public class PlanController {

  private final PlanService planService;
  private final EmailService emailService;

  @Autowired
  public PlanController(PlanService planService, EmailService emailService) {
    this.planService = planService;
    this.emailService = emailService;
  }

  @PostMapping("/plan/get")
  public ResponseEntity<?> getPlanByPlanAppAcronym(@RequestBody PlanEntity plan) {

    Map<String, Object> jsonObject = new HashMap<>();

    if (plan.getPlanappAcronym() == null || plan.getPlanappAcronym().isBlank()) {
      jsonObject = ErrorMgrService.errorHandler("invalid fields", Thread.currentThread().getStackTrace()[1]);
    } else {
      try {
        List<Map<String, Object>> plans = planService.getPlan(plan.getPlanappAcronym());
        emailService.sendEmail("test@email.com", "hehe", "this is the content", "tms@gmail.com");
        return ResponseEntity.ok().body(plans);
      } catch (Exception e) {
        jsonObject = ErrorMgrService.errorHandler(e, Thread.currentThread().getStackTrace()[1]);
      }
    }
    return ResponseEntity.ok(jsonObject);
  }

  @Validated
  @PostMapping("/plan/create")
  public ResponseEntity<?> createNewPlan(@Valid @RequestBody PlanEntity plan, BindingResult bindingResult) {

    Map<String, Object> jsonObject = new HashMap<>();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    LocalDate startDate = LocalDate.parse(plan.getPlanstartDate(), formatter);
    LocalDate endDate = LocalDate.parse(plan.getPlanendDate(), formatter);

    // System.out.println(plan.getPlanMVPname());
    // System.out.println(plan.getPlanappAcronym());
    // System.out.println(plan.getPlanstartDate());
    // System.out.println(plan.getPlanendDate());
    // System.out.println(plan.getPlancolour());

    if (bindingResult.hasErrors()) {
      jsonObject = ErrorMgrService.errorHandler("invalid fields", Thread.currentThread().getStackTrace()[1]);
      List<FieldError> errors = bindingResult.getFieldErrors();
      System.out.println(errors);
    } else if (!startDate.isBefore(endDate)) {
      jsonObject = ErrorMgrService.errorHandler("invalid fields", Thread.currentThread().getStackTrace()[1]);
    } else {
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

  @Validated
  @PostMapping("/plan/update")
  public ResponseEntity<?> updatePlan(@Valid @RequestBody PlanEntity plan, BindingResult bindingResult) {

    Map<String, Object> jsonObject = new HashMap<>();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    LocalDate startDate = LocalDate.parse(plan.getPlanstartDate(), formatter);
    LocalDate endDate = LocalDate.parse(plan.getPlanendDate(), formatter);

    // if empty fields, throw error
    if (bindingResult.hasErrors()) {
      jsonObject = ErrorMgrService.errorHandler("invalid fields", Thread.currentThread().getStackTrace()[1]);
    } else if (!startDate.isBefore(endDate)) {
      jsonObject = ErrorMgrService.errorHandler("invalid fields", Thread.currentThread().getStackTrace()[1]);
    } else {
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