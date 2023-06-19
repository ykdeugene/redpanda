package com.tmsjsb.redpanda.Service;

import java.util.Map;

public interface TaskService {

    Map<String,Object> getAllTask(Map<String,Object> body);
    Map<String,Object> createTask(Map<String,Object> body,String token);
    Map<String,Object> demoteTask(Map<String,Object> body,String token);
    Map<String,Object> promoteTask(Map<String,Object> body,String token);
    Map<String,Object> updateTask(Map<String,Object> body,String token);

}
