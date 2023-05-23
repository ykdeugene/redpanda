package com.tmsjsb.redpanda.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

public class SampleController {

  @GetMapping("/{index}")
  public String simpleHello(@PathVariable int index) {

    return somearray(index);
  }

  private static String somearray(int index) {
    List<String> people = new ArrayList<>();

    people.add("Mike Chang");
    people.add("Adam Lambert");
    people.add("Craig Groeschel");

    System.out.println(people.toString());
    return people.get(index).toString();
  }
  
}
