package com.tmsjsb.redpanda.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmsjsb.redpanda.Service.AuthService;
import com.tmsjsb.redpanda.Service.ErrorMgrService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

  public InterceptorController(AuthService authService) {
    this.authService = authService;
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
    Object body = args[0];
    System.out.println("Request Body Object: " + body);

    String password = null; //for testing
    String taskstate = null;
    String taskid = null;
    String taskappacronym = null;
    String permit = null;

    //System.out.println("Route: " + route);
    if (body instanceof Map) {
      Map<?, ?> requestBody = (Map<?, ?>) body;

    for (Map.Entry<?, ?> entry : requestBody.entrySet()) {
      Object key = entry.getKey();
      Object value = entry.getValue();

      if ("password".equals(key)) {
        password = value.toString();
        //System.out.println("Password: " + password);
      } else if ("Task_state".equals(key)) {
        taskstate = value.toString();
      } else if ("Task_id".equals(key)) {
        taskid = value.toString();
        taskappacronym = taskid.split("_")[0];
        //System.out.println("taskid: " + taskid);
      } else if ("Task_app_Acronym".equals(key)) {
        taskappacronym = value.toString();
      } 
    }
  }
  if (route.contains("task") && !(route.contains("get"))) {
      // check taskid for app concat -> task permits (task_state permit)
    if (taskid.equals(null)){
      // create permit
    }
      // other permits
    isAuth = true; // tempoary
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

  // @Pointcut("execution(* com.tmsjsb.redpanda.Controller..*(..)) && !execution(* com.tmsjsb.redpanda.Controller.AuthController.AuthLogin(..)) && !execution(* com.tmsjsb.redpanda.Controller.AdminController..*(..))")
  // public void interceptedMethods() {}
  
  // @Around("interceptedMethods()")
  // public Object interceptedMethods(ProceedingJoinPoint joinPoint) throws Throwable {
  //   Map<String, Object> errorObject = new HashMap<>();
  //   // Check if the request has a JWT in the header

  //   System.out.println("intercepted normal");
  //   HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
  //   String jwt = request.getHeader("Authorization");
  //   String ipAddress = request.getRemoteAddr();
  //   String browserType = request.getHeader("User-Agent");

  //   if (jwt == null) {
  //     errorObject = ErrorMgrService.errorHandler("Invalid Parameters", Thread.currentThread().getStackTrace()[1]);
  //     return new ResponseEntity<>(errorObject, HttpStatus.OK);
  //   }
    
  //   // Validate the JWT
  //   boolean isValid = false;
  //   isValid = authService.validateJwt(jwt, ipAddress, browserType);

  //   if (!isValid) {
  //     errorObject = ErrorMgrService.errorHandler("Invalid token", Thread.currentThread().getStackTrace()[1]);
  //     return new ResponseEntity<>(errorObject, HttpStatus.OK);   
  //   } else {
  //     return joinPoint.proceed();
  //   }
  // }

  private String extractRequestBody(HttpServletRequest request) throws IOException {
    // Extract the request body from the request object
    // Implement the logic to read the request body based on your specific requirements
    // For example, you can use a library like Jackson or Gson to deserialize the request body into an object
    // In this example, let's assume you are extracting the request body as a String
    // Adapt this method based on your use case
    // Here, we are simply returning the request body as a String
    return request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
  }
}
