package com.kalbob.app.employee.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

  @RequestMapping("/hello")
  public String index() {
    return "Hello from Spring Boot!";
  }

}