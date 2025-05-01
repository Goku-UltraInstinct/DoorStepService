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
    
    @GetMapping("/VendorLogin")
    public String go3(){
    return "VendorLogin";
    }
    
    @GetMapping("/VendorHome")
    public String go4(){
    return "VendorHome";
    }
    
    @GetMapping("/VendorManagePhotos")
    public String go5(){
    return "VendorManagePhotos";
    }
    
    @GetMapping("/EditDetail")
    public String go6(){
    return "EditDetail";
    }
}

