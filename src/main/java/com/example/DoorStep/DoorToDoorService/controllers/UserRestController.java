package com.example.DoorStep.DoorToDoorService.controllers;

import com.example.DoorStep.DoorToDoorService.vmm.DbLoader;
import com.example.DoorStep.DoorToDoorService.vmm.RDBMS_TO_JSON;
import jakarta.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserRestController {

    @PostMapping("/signUp")
    public String userDetails(@RequestParam String uname,
            @RequestParam String uemail,
            @RequestParam String upass,
            @RequestParam MultipartFile uphoto,
            @RequestParam String uaddress,
            @RequestParam String ucontact
    ) {
        try {
            ResultSet rs = DbLoader.executeSQL("select * from user where uemail='" + uemail + "'");
            if (rs.next()) {
                return "exists";
            } else {
                String projectPath = System.getProperty("user.dir");
                String internal_path = "/src/main/resources/static";
                String folderName = "/myUploads";
                String orgName = "/" + uphoto.getOriginalFilename();

                rs.moveToInsertRow();
                rs.updateString("uname", uname);
                rs.updateString("uemail", uemail);
                rs.updateString("upass", upass);
                rs.updateString("uphoto", orgName);
                rs.updateString("uaddress", uaddress);
                rs.updateString("ucontact", ucontact);
                rs.insertRow();

                byte b1[] = uphoto.getBytes();
                FileOutputStream fos = new FileOutputStream(projectPath + internal_path + folderName + orgName);
                fos.write(b1);
                fos.close();

                return "success";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
    }

    @PostMapping("/UserLogin")
    public String login(@RequestParam String email, @RequestParam String pass, HttpSession session) {
        try {
            ResultSet rs = DbLoader.executeSQL("select * from user where uemail='" + email + "' and upass='" + pass + "'");
            if (rs.next()) {
                session.setAttribute("uid", rs.getInt("uid"));
                return "success";
            } else {
                return "failed";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
    }
    
    @GetMapping("/getAllCities")
    public String getCities() {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from cities");

        return ans;
    }
}
