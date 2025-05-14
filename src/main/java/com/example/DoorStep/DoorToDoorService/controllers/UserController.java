package com.example.DoorStep.DoorToDoorService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
   
    @GetMapping("/UserSignUp")
    public String go() {
        return "UserSignUp";
    } 
    
    @GetMapping("/UserLogin")
    public String go2() {
        return "UserLogin";
    }

    @GetMapping("/UserHome")
    public String go3() {
        return "UserHome";
    }
    
    @GetMapping("/ShowServices")
    public String go4() {
        return "ShowServices";
    }
    
    @GetMapping("/ShowAvailableVendors")
    public String go5() {
        return "ShowAvailableVendors";
    }
}
