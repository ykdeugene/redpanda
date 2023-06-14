package com.tmsjsb.redpanda.Service.ServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tmsjsb.redpanda.Entity.AppEntity;
import com.tmsjsb.redpanda.Entity.TaskEntity;
import com.tmsjsb.redpanda.Repository.AppRepository;
import com.tmsjsb.redpanda.Repository.TaskRepository;
import com.tmsjsb.redpanda.Service.ErrorMgrService;
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
        try{
            TaskEntity task = new TaskEntity();
                if((String) body.get("Task_app_Acronym") == null || (String) body.get("Task_plan") == null || (String) body.get("Task_name") == null)
            {
                jsonObject= ErrorMgrService.errorHandler("Invalid Parameters", Thread.currentThread().getStackTrace()[1]);
                return jsonObject;
            }

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
            appRespository.save(thisapp);

            task.setTask_name((String) body.get("Task_name"));
            task.setTask_description((String) body.get("Task_description"));
            task.setTask_plan((String) body.get("Task_plan"));
            task.setTask_app_Acronym((String) body.get("Task_app_Acronym"));
            task.setTask_state("Open");

            //getting Date and Time
            String date = LocalDate.now(ZoneId.of("Asia/Singapore")).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            task.setTask_createDate(date);
            String dateTime = LocalDateTime.now(ZoneId.of("Asia/Singapore")).format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            //System.out.println("\n==============================\nUserID: "+username+"\nState: Open\nDate/Time: "+dateTime+"\n==============================");
            String notes = (String) body.get("Task_notes");
            if(notes != null && !notes.isEmpty())
            {
                notes = "Notes: "+notes+"\n\n\n";
            }
            else
            {
                notes = "";
            }
            task.setTask_notes(notes+"\n==============================\nUserID: "+username+"\nState: Open\nDate/Time: "+dateTime+"\n==============================");
            taskRepository.save(task);
            jsonObject.put("result", "true");
            return jsonObject;
        }catch(Exception e)
        {
            jsonObject = ErrorMgrService.errorHandler("Internal Error", Thread.currentThread().getStackTrace()[1]);
            return jsonObject;
        }

        
    }   
    
}
