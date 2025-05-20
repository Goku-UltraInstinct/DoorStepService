package com.example.DoorStep.DoorToDoorService.controllers;

import jakarta.servlet.http.HttpSession;
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
    public String go2(HttpSession session) {
        Integer vid = (Integer) session.getAttribute("vid");
        if (vid == null) {
            return "redirect:/VendorLogin";
        } else {
            return "AdminManageServiceProviders";
        }
    }

    @GetMapping("/VendorManagePhotos")
    public String go5(HttpSession session) {
        Integer vid = (Integer) session.getAttribute("vid");
        if (vid == null) {
            return "redirect:/VendorLogin";
        } else {
            return "VendorManagePhotos";
        }
    }

    @GetMapping("/EditDetail")
    public String go6(HttpSession session) {
        Integer vid = (Integer) session.getAttribute("vid");
        if (vid == null) {
            return "redirect:/VendorLogin";
        } else {
            return "EditDetail";
        }
    }

    @GetMapping("/UserShowServiceProvidersDetail")
    public String go7(HttpSession session) {
        Integer vid = (Integer) session.getAttribute("vid");
        if (vid == null) {
            return "redirect:/VendorLogin";
        } else {
            return "UserShowServiceProvidersDetail";
        }
    }

    @GetMapping("/VendorManageBookings")
    public String go8(HttpSession session) {
        Integer vid = (Integer) session.getAttribute("vid");
        if (vid == null) {
            return "redirect:/VendorLogin";
        } else {
            return "VendorManageBookings";
        }
    }

//    @GetMapping("/Vendorlogout")
//    public String logout(HttpSession session)
//    {
//        session.invalidate();
//        return "redirect:/";
//    }
}
