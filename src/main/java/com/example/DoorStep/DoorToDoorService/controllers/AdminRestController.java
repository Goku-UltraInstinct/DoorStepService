package com.example.DoorStep.DoorToDoorService.Controllers;

import com.example.DoorStep.DoorToDoorService.vmm.DbLoader;
import com.example.DoorStep.DoorToDoorService.vmm.RDBMS_TO_JSON;
import java.sql.ResultSet;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;

@RestController
public class AdminRestController {

    @PostMapping("/AdminLogin")
    public String login(@RequestParam String email, @RequestParam String pass) {
        try {
            ResultSet rs = DbLoader.executeSQL("select * from admin where email='" + email + "' and pass='" + pass + "'");
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

    @PostMapping("/AdminManageCities")
    public String cityDetails(@RequestParam String cn, @RequestParam MultipartFile cp, @RequestParam String cd) {
        try {
            ResultSet rs = DbLoader.executeSQL("select * from cities where cityname='" + cn + "'");
            if (rs.next()) {
                return "exists";
            } else {
                String projectPath = System.getProperty("user.dir");
                String internal_path = "/src/main/resources/static";
                String folderName = "/myUploads";
                String orgName = "/" + cp.getOriginalFilename();

                rs.moveToInsertRow();
                rs.updateString("cityname", cn);
                rs.updateString("cityphoto", orgName);
                rs.updateString("citydesc", cd);
                rs.insertRow();

                byte b1[] = cp.getBytes();
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

    @PostMapping("/AdminShowCities")
    public String showCities() {
        try {
            String json = new RDBMS_TO_JSON().generateJSON("select * from cities");
            return json;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }

    }

    @PostMapping("/AdminDeleteCity")
    public String deleteCity(@RequestParam String cn) {
        try {
            ResultSet rs = DbLoader.executeSQL("select * from cities where cityname='" + cn + "'");
            if (rs.next()) {
                rs.deleteRow();
                return "success";
            } else {
                return "notfound";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
    }

    @PostMapping("/AdminManageServices")
    public String serviceDetails(@RequestParam String sname, @RequestParam MultipartFile sphoto, @RequestParam String sdesc) {
        try {
            ResultSet rs = DbLoader.executeSQL("select * from services where sname='" + sname + "'");
            if (rs.next()) {
                return "exists";
            } else {
                String projectPath = System.getProperty("user.dir");
                String internal_path = "/src/main/resources/static";
                String folderName = "/myUploads";
                String orgName = "/" + sphoto.getOriginalFilename();

                rs.moveToInsertRow();
                rs.updateString("sname", sname);
                rs.updateString("sphoto", orgName);
                rs.updateString("sdesc", sdesc);
                rs.insertRow();

                byte[] b1 = sphoto.getBytes();
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

    @PostMapping("/AdminShowServices")
    public String showServices() {
        try {
            String json = new RDBMS_TO_JSON().generateJSON("select * from services");
            return json;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
    }

    @PostMapping("/AdminDeleteService")
    public String deleteService(@RequestParam String sname) {
        try {
            ResultSet rs = DbLoader.executeSQL("select * from services where sname='" + sname + "'");
            if (rs.next()) {
                rs.deleteRow();
                return "success";
            } else {
                return "notfound";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
    }

}
