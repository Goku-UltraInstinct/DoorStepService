package com.example.DoorStep.DoorToDoorService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
   
    @GetMapping("/UserSignUp")
    public String go() {
        return "UserSignUp";
    } 
}
