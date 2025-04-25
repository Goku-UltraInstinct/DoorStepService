package com.example.DoorStep.DoorToDoorService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VendorController {

    @GetMapping("/VendorSignUp")
    public String go(){
    return "VendorSignUp";
    }
    
    @GetMapping("/AdminManageServiceProviders")
    public String go2(){
    return "AdminManageServiceProviders";
    }
}

