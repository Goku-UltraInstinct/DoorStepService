package com.example.DoorStep.DoorToDoorService.controllers;

import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/")
    public String go3() {
        return "UserHome";
    }

    @GetMapping("/ShowServices")
    public String go4() {
        return "ShowServices";
    }

    @GetMapping("/ShowAvailableVendors")
    public String go5(HttpSession session) {
        Integer uid = (Integer) session.getAttribute("uid");
        if (uid == null) {
            return "redirect:/UserLogin";
        } else {
            return "ShowAvailableVendors";
        }
//        return "ShowAvailableVendors";
    }

    @GetMapping("/viewslot")
    public String viewslot(HttpSession session) {
        Integer uid = (Integer) session.getAttribute("uid");
        if (uid == null) {
            return "redirect:/UserLogin";
        } else {
            return "viewslots";
        }
    }

    @GetMapping("/payment")
    public String payment(HttpSession session) {
        Integer uid = (Integer) session.getAttribute("uid");
        if (uid == null) {
            return "redirect:/UserLogin";
        } else {
            return "payment";
        }
    }

    @GetMapping("/UserShowBookingHistory")
    public String go6(HttpSession session) {
        Integer uid = (Integer) session.getAttribute("uid");
        if (uid == null) {
            return "redirect:/UserLogin";
        } else {
            return "UserShowBookingHistory";
        }
    }

    @GetMapping("/UserChangePassword")
    public String go7(HttpSession session) {
        Integer uid = (Integer) session.getAttribute("uid");
        if (uid == null) {
            return "redirect:/UserLogin";
        } else {
            return "UserChangePassword";
        }
    }

    @GetMapping("/Userlogout")
    public String logout(HttpSession session) {
        session.removeAttribute("uid");
        return "redirect:/";
    }

}
