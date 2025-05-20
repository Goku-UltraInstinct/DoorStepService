package com.example.DoorStep.DoorToDoorService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VendorController {

    @GetMapping("/VendorSignUp")
    public String go() {
        return "VendorSignUp";
    }

    @GetMapping("/VendorLogin")
    public String go3() {
        return "VendorLogin";
    }

    @GetMapping("/VendorHome")
    public String go4() {
        return "VendorHome";
    }

    @GetMapping("/AdminManageServiceProviders")
    public String go2() {
        return "AdminManageServiceProviders";
    }

    @GetMapping("/VendorManagePhotos")
    public String go5() {
        return "VendorManagePhotos";
    }

    @GetMapping("/EditDetail")
    public String go6() {
        return "EditDetail";
    }

    @GetMapping("/UserShowServiceProvidersDetail")
    public String go7() {
        return "UserShowServiceProvidersDetail";
    }

    @GetMapping("/VendorManageBookings")
    public String go8() {
        return "VendorManageBookings";
    }

//    @GetMapping("/Vendorlogout")
//    public String logout(HttpSession session)
//    {
//        session.invalidate();
//        return "redirect:/";
//    }
}
