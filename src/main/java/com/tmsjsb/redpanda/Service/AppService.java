package com.tmsjsb.redpanda.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.tmsjsb.redpanda.Entity.AppEntity;

public interface AppService {
  List<Map<String,Object>> getAllApps();

  Map<String, Object> createApp(String App_Acronym, String App_Description, Integer App_Rnumber, String App_startDate, String App_endDate, String App_permit_Create, String App_permit_Open, String App_permit_toDoList, String App_permit_Doing, String App_permit_Done);
  Map<String, Object> updateApp(String App_Acronym, String App_Description, String App_startDate, String App_endDate, String App_permit_Create, String App_permit_Open, String App_permit_toDoList, String App_permit_Doing, String App_permit_Done);
  
  public Optional<AppEntity> getAppById(String id);
}
