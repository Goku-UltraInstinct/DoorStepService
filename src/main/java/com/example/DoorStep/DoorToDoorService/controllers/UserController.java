package com.example.DoorStep.DoorToDoorService.controllers;

import org.springframework.web.bind.annotation.GetMapping;

public class UserController {
   @GetMapping("/UserSignUp")
    public String go() {
        return "UserSignUp";
    } 
}
