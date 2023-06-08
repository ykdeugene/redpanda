package com.tmsjsb.redpanda.Controller;

import com.tmsjsb.redpanda.Service.AuthService;
import com.tmsjsb.redpanda.Service.ErrorMgrService;

import java.util.HashMap;
import java.util.Map;

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

  @Pointcut("execution(* com.tmsjsb.redpanda.Controller.AdminController..*(..)) && !execution(* com.tmsjsb.redpanda.Controller.AuthController.*(..))")

  public void interceptedAdminMethods() {}

  @Around("interceptedAdminMethods()")
  public Object interceptedAdminMethods(ProceedingJoinPoint joinPointAdmin) throws Throwable {
    Map<String, Object> errorObject = new HashMap<>();

    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    String jwt = request.getHeader("Authorization");
    String ipAddress = request.getRemoteAddr();
    String browserType = request.getHeader("User-Agent");
      
    if (jwt == null) {
      errorObject = ErrorMgrService.errorHandler("Invalid Parameters", Thread.currentThread().getStackTrace()[1]);
      return new ResponseEntity<>(errorObject, HttpStatus.OK);
    }
    System.out.println("intercepted admin");
    boolean isValid = false; boolean isAdmin = false; 

    String username = authService.TokenToUsername(jwt);

    isValid = authService.validateJwt(jwt, ipAddress, browserType);
    isAdmin = authService.CheckGroup(username, "admin");

    if (!isValid) {
      errorObject = ErrorMgrService.errorHandler("Invalid token", Thread.currentThread().getStackTrace()[1]);
      return new ResponseEntity<>(errorObject, HttpStatus.OK);
    } 
    if (!isAdmin) {
      errorObject = ErrorMgrService.errorHandler("no access", Thread.currentThread().getStackTrace()[1]);
      return new ResponseEntity<>(errorObject, HttpStatus.OK);
    }
    else {
      // Return false or any other response indicating invalid authentication 
      return joinPointAdmin.proceed();
    }
  }

  @Pointcut("execution(* com.tmsjsb.redpanda.Controller..*(..)) && !execution(* com.tmsjsb.redpanda.Controller.AuthController.AuthLogin(..)) && !execution(* com.tmsjsb.redpanda.Controller.AdminController..*(..))")
  public void interceptedMethods() {}
  
  @Around("interceptedMethods()")
  public Object interceptedMethods(ProceedingJoinPoint joinPoint) throws Throwable {
    Map<String, Object> errorObject = new HashMap<>();
    // Check if the request has a JWT in the header

    System.out.println("intercepted normal");
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    String jwt = request.getHeader("Authorization");
    String ipAddress = request.getRemoteAddr();
    String browserType = request.getHeader("User-Agent");

    if (jwt == null) {
      errorObject = ErrorMgrService.errorHandler("Invalid Parameters", Thread.currentThread().getStackTrace()[1]);
      return new ResponseEntity<>(errorObject, HttpStatus.OK);
    }
    
    // Validate the JWT
    boolean isValid = false;
    isValid = authService.validateJwt(jwt, ipAddress, browserType);

    if (!isValid) {
      errorObject = ErrorMgrService.errorHandler("Invalid token", Thread.currentThread().getStackTrace()[1]);
      return new ResponseEntity<>(errorObject, HttpStatus.OK);   
    } else {
      return joinPoint.proceed();
    }
  }
}
  
