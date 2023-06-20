package com.tmsjsb.redpanda.Service;

import java.util.List;
import java.util.Map;

import com.tmsjsb.redpanda.Entity.PlanEntity;

public interface PlanService {

  List<Map<String, Object>> getPlan(String planAppAcronym);

  Map<String, Object> createPlan(PlanEntity plan);

  Map<String, Object> updatePlan(PlanEntity plan);

}
