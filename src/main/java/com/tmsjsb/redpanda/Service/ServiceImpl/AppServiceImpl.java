package com.tmsjsb.redpanda.Service.ServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.ErrorManager;

import org.springframework.stereotype.Service;

import com.tmsjsb.redpanda.Entity.AppEntity;
import com.tmsjsb.redpanda.Entity.UserEntity;
import com.tmsjsb.redpanda.Repository.AppRepository;
import com.tmsjsb.redpanda.Service.AppService;
import com.tmsjsb.redpanda.Service.ErrorMgrService;

@Service
public class AppServiceImpl implements AppService {

  private final AppRepository appRepository;

  private AppServiceImpl(AppRepository appRepository) {
    this.appRepository = appRepository;
  }

  @Override
  public List<Map<String,Object>> getAllApps() {

    List<AppEntity> apps = appRepository.findAll();
    System.out.println(apps);
    List<Map<String,Object>> newList = new ArrayList<>();
    
    for(int i = 0; i < apps.size();i++)
    {
      Map<String,Object> temp = new HashMap<>(0);
      temp.put("App_Acronym", apps.get(i).getApp_Acronym());
      temp.put("App_Description", apps.get(i).getApp_Description());
      temp.put("App_Rnumber", apps.get(i).getApp_Rnumber());
      temp.put("App_startDate", apps.get(i).getApp_startDate());
      temp.put("App_endDate", apps.get(i).getApp_endDate());
      temp.put("App_permit_Create", apps.get(i).getApp_permit_Create());
      temp.put("App_permit_Open", apps.get(i).getApp_permit_Open());
      temp.put("App_permit_toDoList", apps.get(i).getApp_permit_toDoList());
      temp.put("App_permit_Doing", apps.get(i).getApp_permit_Doing());
      temp.put("App_permit_Done", apps.get(i).getApp_permit_Done());
      newList.add(temp);
    }

    return newList;
  }
  
  @Override
  public Optional<AppEntity> getAppById(String App_Acronym) {
    return appRepository.findById(App_Acronym);
  }

  @Override
  public Map<String, Object> createApp(String App_Acronym, String App_Description, Integer App_Rnumber, String App_startDate, String App_endDate, String App_permit_Create, String App_permit_Open, String App_permit_toDoList, String App_permit_Doing, String App_permit_Done) {
    String result = "true";
    Map<String, Object> jsonObject = new HashMap<>(0);

    try {
      Optional<AppEntity> CheckApp = getAppById(App_Acronym);

      if (CheckApp.isPresent()) {
        jsonObject = ErrorMgrService.errorHandler("data exists", Thread.currentThread().getStackTrace()[1]);
        return jsonObject;
      }

      AppEntity newApp = new AppEntity();
      newApp.setApp_Acronym(App_Acronym);
      newApp.setApp_Description(App_Description);
      newApp.setApp_Rnumber(App_Rnumber);
      newApp.setApp_startDate(App_startDate);
      newApp.setApp_endDate(App_endDate);
      newApp.setApp_permit_Create(App_permit_Create);
      newApp.setApp_permit_Open(App_permit_Open);
      newApp.setApp_permit_toDoList(App_permit_toDoList);
      newApp.setApp_permit_Doing(App_permit_Doing);
      newApp.setApp_permit_Done(App_permit_Done);
      appRepository.save(newApp);
    } catch (Exception e) {
      jsonObject = ErrorMgrService.errorHandler(e, Thread.currentThread().getStackTrace()[1]);
      return jsonObject;
    }

    jsonObject.put("result", result);
    return jsonObject;
  }
}
