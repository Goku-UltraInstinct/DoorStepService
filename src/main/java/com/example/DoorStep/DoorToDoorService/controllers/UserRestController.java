package com.example.DoorStep.DoorToDoorService.controllers;

import com.example.DoorStep.DoorToDoorService.vmm.DbLoader;
import java.sql.ResultSet;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class UserRestController {
    @PostMapping("/UserSignUp")
    public String login(@RequestParam String uemail, @RequestParam String upass) {
        try {
            ResultSet rs = DbLoader.executeSQL("select * from user where uemail='" + uemail + "' and upass='" + upass + "'");
            if (rs.next()) {
                return "success";
            } else {
                return "failed";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
    }
}
