package com.tmsjsb.redpanda.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class ErrorMgrService {
  
  public static Map<String, Object> errorHandler(Throwable throwable, StackTraceElement location) {
    Map<String, Object> errorCode = new HashMap<>();
    errorCode.put("results", "uncaught error");

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    throwable.printStackTrace(printWriter);
  
    if (stringWriter.toString().contains("ConstraintViolationImpl")) {
      errorCode.clear();
      errorCode.put("results", "BSJ372");
    }

    LocalTime currentTime = LocalTime.now();

    System.out.println(
    "===============================\n" +
    "========== Error Log ==========\n" +
    "===============================\n" +
    "Time: " + currentTime + "\n" +
    "Loc: " + location + "\n" +
    "DevMode" + stringWriter.toString()
    );

    return errorCode;
  }

  public static Map<String, Object> errorHandler(String throwable, StackTraceElement location) {
    Map<String, Object> errorCode = new HashMap<>();
    errorCode.put("results", "uncaught error");
  
    if (throwable == "user not found") {
      errorCode.clear();
      errorCode.put("results", "BSJxxx");
    } else if (throwable == "data exists") {
      errorCode.clear();
      errorCode.put("results", "BSJ375");
    } else if (throwable == "invalid credentials") {
      errorCode.clear();
      errorCode.put("results", "BSJxxx");
    }

    LocalTime currentTime = LocalTime.now();

    System.out.println(
    "===============================\n" +
    "========== Error Log ==========\n" +
    "===============================\n" +
    "Time: " + currentTime + "\n" +
    "Loc: " + location + "\n"
    );

    return errorCode;
  }
}