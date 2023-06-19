package com.tmsjsb.redpanda.Controller;

import com.tmsjsb.redpanda.Entity.AppEntity;
import com.tmsjsb.redpanda.Entity.TaskEntity;
import com.tmsjsb.redpanda.Repository.AppRepository;
import com.tmsjsb.redpanda.Repository.TaskRepository;
import com.tmsjsb.redpanda.Service.AuthService;
import com.tmsjsb.redpanda.Service.ErrorMgrService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class InterceptorController {

  private final AuthService authService;
  private TaskRepository taskRepository;
  private AppRepository appRespository;

  public InterceptorController(AuthService authService, AppRepository appRespository, TaskRepository taskRepository) {
    this.authService = authService;
    this.taskRepository = taskRepository;
    this.appRespository = appRespository;
  }


  @Pointcut("execution(* com.tmsjsb.redpanda.Controller..*(..)) && !execution(* com.tmsjsb.redpanda.Controller.AuthController.AuthLogin(..)) && args(.., *)")
  public void interceptedAdminMethods() {}

  @Around("interceptedAdminMethods()")
  public Object interceptedAdminMethods(ProceedingJoinPoint joinPointAdmin) throws Throwable {
    Map<String, Object> errorObject = new HashMap<>();
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

    String jwt = request.getHeader("Authorization");
    String ipAddress = request.getRemoteAddr();
    String browserType = request.getHeader("User-Agent");  
    boolean isValid = false; boolean isAuth = false; 

    if (jwt == null) {
      errorObject = ErrorMgrService.errorHandler("Invalid Parameters", Thread.currentThread().getStackTrace()[1]);
      return new ResponseEntity<>(errorObject, HttpStatus.OK);
    }
    
    String username = authService.TokenToUsername(jwt);
    isValid = authService.validateJwt(jwt, ipAddress, browserType);
    
    if (!isValid) {
      errorObject = ErrorMgrService.errorHandler("Invalid token", Thread.currentThread().getStackTrace()[1]);
      return new ResponseEntity<>(errorObject, HttpStatus.OK);
    } 
  
    String route = request.getRequestURI();
    Object[] args = joinPointAdmin.getArgs();
    System.out.println(args.length);
    Object body = args[args.length-1];
    System.out.println("Request Body Object: " + body);

    String taskstate = null;
    String taskid = null;
    String taskappacronym = null;
    String permit = null;

    if (body instanceof Map) {
      Map<?, ?> requestBody = (Map<?, ?>) body;

    for (Map.Entry<?, ?> entry : requestBody.entrySet()) {
      Object key = entry.getKey();
      Object value = entry.getValue();

      if ("Task_id".equals(key)) {
        taskid = value.toString();
        //System.out.println("split: " + taskid.split("_")[0]);
        taskappacronym = taskid.split("_")[0];
        //System.out.println("taskid: " + taskid);
      } else if ("Task_app_Acronym".equals(key)) {
        taskappacronym = value.toString();
      } 
    }
  }
  if (route.contains("task") && !(route.contains("get"))) {
    System.out.println("app: " + taskappacronym);
    Optional<AppEntity> app = appRespository.findById(taskappacronym);
    AppEntity thisapp = app.get();
      // check taskid for app concat -> task permits (task_state permit)
    if (taskid == null){
      // create permit
      permit = thisapp.getApp_permit_Create();  
    }
    // other permits
    else{
      Optional<TaskEntity> task = taskRepository.findById(taskid);
      TaskEntity thistask = task.get();
      taskstate = thistask.getTask_state();
      switch(taskstate){
        case "Open":
        permit = thisapp.getApp_permit_Open(); 
        break;
        case "To_do":
        permit = thisapp.getApp_permit_toDoList(); 
        break;
        case "Doing":
        permit = thisapp.getApp_permit_Doing(); 
        break;
        case "Done":
        permit = thisapp.getApp_permit_Done();
        break; 
        default:
        //some error on taskstate
      }
    }
    if(permit == null) isAuth = false;
    else isAuth = authService.CheckGroup(username, permit);
    //isAuth = true; // tempoary
      System.out.println("Executing in TaskController");
  } else if (route.contains("app") && !(route.contains("get"))) {
    isAuth = authService.CheckGroup(username, "Project Lead");
      System.out.println("Executing in AppController");
  } else if (route.contains("plan") && !(route.contains("get"))) {
    isAuth = authService.CheckGroup(username, "Project Manager");
      System.out.println("Executing in PlanController");
  } else if (route.contains("admin")){
    isAuth = authService.CheckGroup(username, "admin");
      System.out.println("Executing in Admin");
  }
  else isAuth = true;

    if (!isAuth) {
      errorObject = ErrorMgrService.errorHandler("no access", Thread.currentThread().getStackTrace()[1]);
      return new ResponseEntity<>(errorObject, HttpStatus.OK);
    }
    else {
      // Return false or any other response indicating invalid authentication 
      return joinPointAdmin.proceed();
    }
  }
}
