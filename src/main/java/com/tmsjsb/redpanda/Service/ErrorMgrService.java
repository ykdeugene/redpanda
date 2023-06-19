package com.tmsjsb.redpanda.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class ErrorMgrService {

  public static Map<String, Object> errorHandler(Throwable throwable, StackTraceElement location) {
    Map<String, Object> errorCode = new HashMap<>();
    errorCode.put("result", "BSJ400");

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    throwable.printStackTrace(printWriter);

    if (stringWriter.toString().contains("ConstraintViolationImpl")) {
      errorCode.clear();
      errorCode.put("result", "BSJ372");
    }

    LocalTime currentTime = LocalTime.now();

    System.out.println(
      "===============================\n" +
      "========== Error Log ==========\n" +
      "===============================\n" +
      "Time: " + currentTime + "\n" +
      "Loc: " + location + "\n" +
      "DevMode" + stringWriter.toString());

    return errorCode;
  }

  public static Map<String, Object> errorHandler(String throwable, StackTraceElement location) {
    Map<String, Object> errorCode = new HashMap<>();

    if (throwable == "user not found") {
      errorCode.put("result", "BSJxxx");
    } else if (throwable == "Invalid Parameters") {
      errorCode.put("result", "BSJ369b");
    } else if (throwable == "Invalid token") {
      errorCode.put("result", "BSJ370");
    } else if (throwable == "invalid credentials") {
      errorCode.put("result", "BSJ371");
    } else if (throwable == "invalid fields") {
      errorCode.put("result", "BSJ372");
    } else if (throwable == "no access") {
      errorCode.put("result", "BSJ373");
    } else if (throwable == "data not found") {
      errorCode.put("result", "BSJ374");
    } else if (throwable == "data exists") {
      errorCode.put("result", "BSJ375");
    } else if (throwable == "Invalid action") {
      errorCode.put("result", "BSJ-376");
    } else {
      errorCode.put("result", "BSJ400");
    }

    LocalTime currentTime = LocalTime.now();

    System.out.println(
      "===============================\n" +
      "========== Error Log ==========\n" +
      "===============================\n" +
      "Time: " + currentTime + "\n" +
      "Loc: " + location + "\n");

    return errorCode;
  }
}

// jsonObject = ErrorMgrService.errorHandler("invalid fields",
// Thread.currentThread().getStackTrace()[1]);
