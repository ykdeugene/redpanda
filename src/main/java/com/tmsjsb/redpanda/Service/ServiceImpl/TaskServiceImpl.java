package com.tmsjsb.redpanda.Service.ServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public Map<String,Object> getAllTask(Map<String,Object> body)
    {
        Map<String,Object> jsonObject = new HashMap<>(0);
        if((String) body.get("Task_app_Acronym") == null)
        {
            jsonObject= ErrorMgrService.errorHandler("Invalid Parameters", Thread.currentThread().getStackTrace()[1]);
            return jsonObject;
        }
        List<TaskEntity> task = taskRepository.findByTaskappAcronym((String) body.get("Task_app_Acronym"));
        Map<String, Object> entityMap = task.stream()
            .collect(Collectors.toMap(TaskEntity::getTask_id, entity -> entity));

        return entityMap;
    }

    @Override
    public Map<String,Object> createTask(Map<String,Object> body, String token)
    {
        Map<String,Object> jsonObject = new HashMap<>(0);
        try{
            TaskEntity task = new TaskEntity();
            if((String) body.get("Task_app_Acronym") == null || (String) body.get("Task_plan") == null || (String) body.get("Task_name") == null || !isValidParameter(body))
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
            task.setTaskappAcronym((String) body.get("Task_app_Acronym"));
            task.setTask_state("Open");

            //getting Date and Time
            String date = LocalDate.now(ZoneId.of("Asia/Singapore")).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            task.setTask_createDate(date);
            String dateTime = LocalDateTime.now(ZoneId.of("Asia/Singapore")).format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            //System.out.println("\n==============================\nUserID: "+username+"\nState: Open\nDate/Time: "+dateTime+"\n==============================");
            String notes = (String) body.get("Task_notes");
            if(notes != null && !notes.isEmpty())
            {
                notes = "UserID: "+username+"\nState: Open\nDate/Time: "+dateTime+"\nNotes: "+notes+"\n\n\n";
            }
            else
            {
                notes = "";
            }
            task.setTask_notes(notes+"\n==============================\nTask Created\nUserID: "+username+"\nState: Open\nDate/Time: "+dateTime+"\n==============================");
            taskRepository.save(task);
            jsonObject.put("result", "true");
            return jsonObject;
        }catch(Exception e)
        {
            jsonObject = ErrorMgrService.errorHandler("Internal Error", Thread.currentThread().getStackTrace()[1]);
            return jsonObject;
        }
    }

    @Override
    public Map<String,Object> demoteTask(Map<String,Object> body, String token)
    {
        Map<String,Object> jsonObject = new HashMap<>(0);
        try{
            if((String) body.get("Task_id") == null || !isValidParameter(body))
            {
                jsonObject= ErrorMgrService.errorHandler("invalid fields", Thread.currentThread().getStackTrace()[1]);
                return jsonObject;
            }

            token = token.split(" ")[1];
            DecodedJWT decoded = JWT.decode(token);
            String username = decoded.getClaim("username").asString();

            //retrieve task and check state of task
            TaskEntity task = taskRepository.findById((String) body.get("Task_id")).get();
            if("Open".equals(task.getTask_state()) || "To_do".equals(task.getTask_state()) || "Close".equals(task.getTask_state()))
            {
                jsonObject= ErrorMgrService.errorHandler("Invalid action", Thread.currentThread().getStackTrace()[1]);
                return jsonObject;
            }

            //ArrayList states = new ArrayList<>();
            

            return jsonObject;
        }catch(Exception e)
        {
            jsonObject = ErrorMgrService.errorHandler("Internal Error", Thread.currentThread().getStackTrace()[1]);
            return jsonObject;
        }
    }

    @Override
    public Map<String,Object> promoteTask(Map<String,Object> body, String token)
    {
        Map<String,Object> jsonObject = new HashMap<>(0);
        try{
            if((String) body.get("Task_id") == null || !isValidParameter(body))
            {
                jsonObject= ErrorMgrService.errorHandler("invalid fields", Thread.currentThread().getStackTrace()[1]);
                return jsonObject;
            }
            
            token = token.split(" ")[1];
            DecodedJWT decoded = JWT.decode(token);
            String username = decoded.getClaim("username").asString();

            //retrieve task and application
            if(!permitted(body,username))
            {
                jsonObject= ErrorMgrService.errorHandler("no access", Thread.currentThread().getStackTrace()[1]);
                return jsonObject;
            }
            
            return jsonObject;
        }catch(Exception e)
        {
            jsonObject = ErrorMgrService.errorHandler("Internal Error", Thread.currentThread().getStackTrace()[1]);
            return jsonObject;
        }
    }

    @Override
    public Map<String,Object> updateTask(Map<String,Object> body, String token)
    {
        Map<String,Object> jsonObject = new HashMap<>(0);
        try{
            if((String) body.get("Task_id") == null || !isValidParameter(body))
            {
                jsonObject= ErrorMgrService.errorHandler("invalid fields", Thread.currentThread().getStackTrace()[1]);
                return jsonObject;
            }

            token = token.split(" ")[1];
            DecodedJWT decoded = JWT.decode(token);
            String username = decoded.getClaim("username").asString();

            //retrieve task and application
            if(!permitted(body,username))
            {
                jsonObject= ErrorMgrService.errorHandler("no access", Thread.currentThread().getStackTrace()[1]);
                return jsonObject;
            }
            
            return jsonObject;
        }catch(Exception e)
        {
            jsonObject = ErrorMgrService.errorHandler("Internal Error", Thread.currentThread().getStackTrace()[1]);
            return jsonObject;
        }
    }

    private boolean permitted(Map<String,Object> body,String username)
    {
        TaskEntity task = taskRepository.findById((String) body.get("Task_id")).get();
        AppEntity app = appRespository.findById(task.getTaskappAcronym()).get();
        switch(task.getTask_state())
        {
            case "Open":
                if(username.equals(app.getApp_permit_Open()))
                {
                    return true;
                }
                break;
            
            case "To_do":
                if(username.equals(app.getApp_permit_toDoList()))
                {
                    return true;
                }
                break;
            
            case "Doing":
                if(username.equals(app.getApp_permit_Doing()))
                {
                    return true;
                }
                break;

            case "Done":
                if(username.equals(app.getApp_permit_Done()))
                {
                    return true;
                }
                break;
            
            default:
                return false;
        }
        return false;
    }
    
    private boolean isValidParameter(Map<String,Object> body)
    {
        for(Map.Entry<String,Object> entry : body.entrySet())
        {
            if(entry.getKey() == "Task_id")
            {
                continue;
            }
            if(!entry.getValue().toString().matches("^[a-zA-Z0-9 ]*$"))
            {
                return false;
            }
        }
        return true;
    }
    
}
