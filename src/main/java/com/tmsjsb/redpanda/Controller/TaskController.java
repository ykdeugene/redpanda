package com.tmsjsb.redpanda.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmsjsb.redpanda.Service.ErrorMgrService;
import com.tmsjsb.redpanda.Service.TaskService;

@RequestMapping("/task")
@RestController
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService)
    {
        this.taskService = taskService;
    }

    // get task by application
    @GetMapping("/get")
    public ResponseEntity<?> getAllTask()
    {
        return ResponseEntity.ok().body(taskService.getAllTask());
    }

    @PostMapping("/createTask")
    public ResponseEntity<?> createTask(@RequestHeader(value = "authorization", required = false) String token,@RequestBody Map<String, Object> requestBody)
    {
        return ResponseEntity.ok().body(taskService.createTask(requestBody,token));
    }

    @PostMapping("/updateTask")
    public ResponseEntity<?> updateTask(@RequestHeader(value = "authorization", required = false) String token,@RequestBody Map<String, Object> requestBody)
    {
        Map<String,Object> jsonObject = new HashMap<>(0);
        if("demote".equals((String) requestBody.get("Option")))
        {
            return ResponseEntity.ok().body(taskService.demoteTask(requestBody, token));
            // System.out.println("demote");
        }
        else if("promote".equals((String) requestBody.get("Option")))
        {
            System.out.println("promote");
        }
        else if("update".equals((String) requestBody.get("Option")))
        {
            System.out.println("promote");
        }
        else{
            jsonObject = ErrorMgrService.errorHandler("Invalid Parameters", Thread.currentThread().getStackTrace()[1]);
        }
        return ResponseEntity.ok().body(jsonObject);
    }
}
