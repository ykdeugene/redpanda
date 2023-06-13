package com.tmsjsb.redpanda.Service.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tmsjsb.redpanda.Entity.AppEntity;
import com.tmsjsb.redpanda.Entity.TaskEntity;
import com.tmsjsb.redpanda.Repository.AppRepository;
import com.tmsjsb.redpanda.Repository.TaskRepository;
import com.tmsjsb.redpanda.Service.TaskService;

import lombok.AllArgsConstructor;

@Service
public class TaskServiceImpl implements TaskService{
    
    private TaskRepository taskRepository;
    private AppRepository appRespository;

    @Autowired
    private TaskServiceImpl(TaskRepository taskRepository, AppRepository appRespository)
    {
        this.taskRepository = taskRepository;
        this.appRespository = appRespository;
    }

    //dummy api
    @Override
    public List<TaskEntity> getAllTask()
    {
        return taskRepository.findAll();
    }

    @Override
    public Map<String,Object> createTask(Map<String,Object> body, String token)
    {
        Map<String,Object> jsonObject = new HashMap<>(0);
        TaskEntity task = new TaskEntity();

        //setting creator and owner
        token = token.split(" ")[1];
        DecodedJWT decoded = JWT.decode(token);
        String username = decoded.getClaim("username").asString();
        task.setTask_creator(username);
        task.setTask_owner(username);

        //getting and setting Rnumber
        Optional<AppEntity> app = appRespository.findById((String) body.get("Task_app_Acronym"));
        AppEntity thisapp = app.get();
        int rNumber = thisapp.getApp_Rnumber();
        task.setTask_id((String) body.get("Task_app_Acronym")+"_"+rNumber);
        thisapp.setApp_Rnumber(rNumber+1);
        //appRespository.save(thisapp);

        task.setTask_name((String) body.get("Task_name"));
        task.setTask_description((String) body.get("Task_description"));
        task.setTask_plan((String) body.get("Task_plan"));
        task.setTask_app_Acronym((String) body.get("Task_app_Acronym"));


        return jsonObject;
    }   
    
}
