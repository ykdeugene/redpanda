package com.tmsjsb.redpanda.Controller;

import com.tmsjsb.redpanda.Service.AuthService;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class MiddlewareController {

  private final AuthService authService;

  public MiddlewareController(AuthService authService) {
    this.authService = authService;
  }

  @Pointcut("execution(* com.tmsjsb.redpanda.Controller..*(..)) && !execution(* com.tmsjsb.redpanda.Controller.AuthController.*(..))")
    public void interceptedMethods() {}
    
    @Around("interceptedMethods()")
    public Object intercept(ProceedingJoinPoint joinPoint) throws Throwable {
        // Check if the request has a JWT in the header
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String jwt = request.getHeader("Authorization");

        System.out.println("something" + jwt);
        // Validate the JWT
        boolean isValid = false;
        if(jwt != null){
        isValid = authService.validateJwt(jwt);
        }

        if (isValid) {
            // Proceed with the execution of the intercepted method
            return joinPoint.proceed();
        } else {
            // Return false or any other response indicating invalid authentication
            return new ResponseEntity<>("KEE370", HttpStatus.OK);
        }
    }
  }
