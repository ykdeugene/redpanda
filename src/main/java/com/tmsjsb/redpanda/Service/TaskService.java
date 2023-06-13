package com.tmsjsb.redpanda.Service;

import java.util.List;
import java.util.Map;

import com.tmsjsb.redpanda.Entity.TaskEntity;

public interface TaskService {

    //dummy api
    List<TaskEntity> getAllTask();
    Map<String,Object> createTask(Map<String,Object> body,String token);

}
