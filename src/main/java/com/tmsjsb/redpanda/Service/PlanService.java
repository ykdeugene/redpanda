package com.tmsjsb.redpanda.Service;

import java.util.List;
import java.util.Map;

public interface PlanService {

  List<Map<String, Object>> getPlan(String planAppAcronym);

}
