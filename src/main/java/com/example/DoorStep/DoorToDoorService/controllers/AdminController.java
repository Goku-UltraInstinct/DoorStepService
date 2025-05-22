/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.DoorStep.DoorToDoorService.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/AdminLogin")
    public String go() {
        return "AdminLogin";
    }

    @GetMapping("/AdminHome")
    public String go1() {
        return "AdminHome";
    }

    @GetMapping("/AdminManageCities")
    public String go2() {
        return "AdminManageCities";
    }

    @GetMapping("/AdminManageServices")
    public String go3() {
        return "AdminManageServices";
    }

    @GetMapping("/Adminlogout")
    public String logout(HttpSession session) {
        session.removeAttribute("email");
        return "redirect:/";
    }
}
