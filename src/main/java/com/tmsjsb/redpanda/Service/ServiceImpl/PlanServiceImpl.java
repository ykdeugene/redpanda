package com.tmsjsb.redpanda.Service.ServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmsjsb.redpanda.Entity.AppEntity;
import com.tmsjsb.redpanda.Entity.PlanEntity;
import com.tmsjsb.redpanda.Repository.AppRepository;
import com.tmsjsb.redpanda.Repository.PlanRepository;
import com.tmsjsb.redpanda.Service.ErrorMgrService;
import com.tmsjsb.redpanda.Service.PlanService;

@Service
public class PlanServiceImpl implements PlanService {

  private final PlanRepository planRepository;

  private final AppRepository appRepository;

  @Autowired
  private PlanServiceImpl(PlanRepository planRepository, AppRepository appRepository) {
    this.planRepository = planRepository;
    this.appRepository = appRepository;
  }

  @Override
  public List<Map<String, Object>> getPlan(String planAppAcronym) {

    List<PlanEntity> plans = planRepository.findByPlanappAcronym(planAppAcronym);
    List<Map<String, Object>> allPlansList = new ArrayList<>();

    for (int i = 0; i < plans.size(); i++) {
      Map<String, Object> temp = new HashMap<>(0);
      temp.put("Plan_MVP_name", plans.get(i).getPlanMVPname());
      temp.put("Plan_startDate", plans.get(i).getPlanstartDate());
      temp.put("Plan_endDate", plans.get(i).getPlanendDate());
      temp.put("Plan_appAcronym", plans.get(i).getPlanappAcronym());
      temp.put("Plan_colour", plans.get(i).getPlancolour());

      allPlansList.add(temp);
    }

    return allPlansList;
  }

  @Override
  public Map<String, Object> createPlan(PlanEntity plan) {

    Map<String, Object> jsonObject = new HashMap<>(0);

    // see if it exists in db first
    List<PlanEntity> checkIfPlanExist = planRepository.findByPlanappAcronymAndPlanMVPname(plan.getPlanappAcronym(),
        plan.getPlanMVPname());
    if (!checkIfPlanExist.isEmpty()) {
      jsonObject = ErrorMgrService.errorHandler("data exists", Thread.currentThread().getStackTrace()[1]);
    } else {
      // check if app acronym exists first
      Optional<AppEntity> checkIfAppExist = appRepository.findById(plan.getPlanappAcronym());
      if (checkIfAppExist.isPresent()) {
        try {
          planRepository.save(plan);
          jsonObject.put("result", "true");
        } catch (Exception e) {
          jsonObject = ErrorMgrService.errorHandler(e, Thread.currentThread().getStackTrace()[1]);
        }
      } else {
        jsonObject = ErrorMgrService.errorHandler("data not found", Thread.currentThread().getStackTrace()[1]);
      }

    }
    return jsonObject;
  }

  @Override
  public Map<String, Object> updatePlan(PlanEntity plan) {
    Map<String, Object> jsonObject = new HashMap<>(0);

    // see if it exists in db first
    List<PlanEntity> checkIfPlanExist = planRepository.findByPlanappAcronymAndPlanMVPname(plan.getPlanappAcronym(),
        plan.getPlanMVPname());
    if (checkIfPlanExist.isEmpty()) {
      jsonObject = ErrorMgrService.errorHandler("data not found", Thread.currentThread().getStackTrace()[1]);
    } else {
      try {
        planRepository.save(plan);
        jsonObject.put("result", "true");
      } catch (Exception e) {
        jsonObject = ErrorMgrService.errorHandler(e, Thread.currentThread().getStackTrace()[1]);
      }
    }
    return jsonObject;
  }

}
