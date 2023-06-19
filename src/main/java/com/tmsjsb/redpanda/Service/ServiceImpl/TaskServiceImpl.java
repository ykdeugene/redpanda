package com.tmsjsb.redpanda.Service.ServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.tmsjsb.redpanda.Entity.GroupsEntity;
import com.tmsjsb.redpanda.Entity.TaskEntity;
import com.tmsjsb.redpanda.Entity.UserEntity;
import com.tmsjsb.redpanda.Repository.AppRepository;
import com.tmsjsb.redpanda.Repository.GroupsRepository;
import com.tmsjsb.redpanda.Repository.TaskRepository;
import com.tmsjsb.redpanda.Repository.UserRepository;
import com.tmsjsb.redpanda.Service.ErrorMgrService;
import com.tmsjsb.redpanda.Service.TaskService;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
public class TaskServiceImpl implements TaskService{
    
    private TaskRepository taskRepository;
    private AppRepository appRespository;
    private GroupsRepository groupsRepository;
    private UserRepository userRepository;

    private final JavaMailSender mailSender;

    @Autowired
    private TaskServiceImpl(TaskRepository taskRepository, AppRepository appRespository,GroupsRepository groupsRepository,UserRepository userRepository,JavaMailSender mailSender)
    {
        this.taskRepository = taskRepository;
        this.appRespository = appRespository;
        this.userRepository = userRepository;
        this.groupsRepository = groupsRepository;
        this.mailSender = mailSender;
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

            ArrayList<String> states = new ArrayList<>(Arrays.asList("Open", "To_do", "Doing", "Done", "Close"));
            int index = states.indexOf(task.getTask_state());
            task.setTask_state(states.get(index-1));
            task.setTask_owner(username);
            if((String) body.get("Task_plan") != null)
            {
                task.setTask_plan((String) body.get("Task_plan"));
            }

            task.setTask_notes(addNotesWithAudit("Task Demoted",task.getTask_notes(),(String) body.get("Task_notes"),states,index,username));
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

            //retrieve task and check state of task
            TaskEntity task = taskRepository.findById((String) body.get("Task_id")).get();
            if("Close".equals(task.getTask_state()))
            {
                jsonObject= ErrorMgrService.errorHandler("Invalid action", Thread.currentThread().getStackTrace()[1]);
                return jsonObject;
            }

            ArrayList<String> states = new ArrayList<>(Arrays.asList("Open", "To_do", "Doing", "Done", "Close"));
            int index = states.indexOf(task.getTask_state());
            task.setTask_state(states.get(index+1));
            task.setTask_owner(username);
            if((String) body.get("Task_plan") != null)
            {
                task.setTask_plan((String) body.get("Task_plan"));
            }

            task.setTask_notes(addNotesWithAudit("Task Promoted",task.getTask_notes(),(String) body.get("Task_notes"),states,index,username));
            taskRepository.save(task);

            //sending email
            if(states.get(index+1).equals("Done"))
            {
                AppEntity app = appRespository.findById(task.getTask_app_Acronym()).get();
                List<GroupsEntity> groups = groupsRepository.findByGroupName(app.getApp_permit_Done());
                
                //ArrayList<String> emailList = new ArrayList<>();
                SimpleMailMessage message = new SimpleMailMessage();
                message.setSubject(task.getTask_id() + " is ready for approval");
                message.setText(task.getTask_id() + " is ready for approval");
                for(int i = 0; i < groups.size();i++)
                {
                    UserEntity user = userRepository.findById(groups.get(i).getUsername()).get();
                    message.setTo(user.getEmail());
                    mailSender.send(message);
                    System.out.println("Email sent to : "+user.getEmail());
                    //emailList.add(user.getEmail());
                }
                
            }
            
            jsonObject.put("result", "true");
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

            //retrieve task and check state of task
            TaskEntity task = taskRepository.findById((String) body.get("Task_id")).get();
            ArrayList<String> states = new ArrayList<>(Arrays.asList("Open", "To_do", "Doing", "Done", "Close"));
            int index = states.indexOf(task.getTask_state());

            task.setTask_owner(username);
            if((String) body.get("Task_plan") != null)
            {
                task.setTask_plan((String) body.get("Task_plan"));
            }
            task.setTask_notes(addNotesWithAudit("Task Updated",task.getTask_notes(),(String) body.get("Task_notes"),states,index,username));
            taskRepository.save(task);
            jsonObject.put("result", "true");
            
            return jsonObject;
        }catch(Exception e)
        {
            jsonObject = ErrorMgrService.errorHandler("Internal Error", Thread.currentThread().getStackTrace()[1]);
            return jsonObject;
        }
    }

    private String addNotesWithAudit(String option,String previous_notes,String notes, ArrayList<String> states,int index, String username)
    {
        String dateTime = LocalDateTime.now(ZoneId.of("Asia/Singapore")).format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        //System.out.println(index);
        if(notes != null && !notes.isEmpty())
        {
            notes = "\nNotes: "+notes+"\n\n\n";
        }
        else
        {
            notes = "";
        }
        String state_note = "";
        if(option.equals("Task Demoted"))
        {
            state_note = " from " + states.get(index) + " to " + states.get(index-1);
        }
        else if(option.equals("Task Promoted")){
            state_note = " from " + states.get(index) + " to " + states.get(index+1);
        }

        notes = notes+"\n\n"+ option + state_note+ "\nUserID: "+ username +"\nState: " + states.get(index) +"\nDate/Time: "+dateTime+"\n==============================\n" + previous_notes;
        // System.out.println("|||||||");
        // System.out.println(notes);
        // System.out.println("|||||||");
        return notes;
    }

    private boolean permitted(Map<String,Object> body,String username)
    {
        TaskEntity task = taskRepository.findById((String) body.get("Task_id")).get();
        AppEntity app = appRespository.findById(task.getTask_app_Acronym()).get();
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
