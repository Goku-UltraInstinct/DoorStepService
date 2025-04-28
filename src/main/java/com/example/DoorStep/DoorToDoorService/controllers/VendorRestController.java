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
public class VendorRestController {

    @GetMapping("/getCities")
    public String getCities() {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from cities");

        return ans;
    }

    @GetMapping("/getServices")
    public String getServices() {
        String ans = new RDBMS_TO_JSON().generateJSON("select * from services");

        return ans;
    }

    @PostMapping("/VendorSignUp")
    public String vendorDetails(@RequestParam String vname,
            @RequestParam String vemail,
            @RequestParam String vcity,
            @RequestParam String vpass,
            @RequestParam String vservice,
            @RequestParam String vsubservice,
            @RequestParam String vstartTime,
            @RequestParam String vendTime,
            @RequestParam String vprice,
            @RequestParam String vcontact,
            @RequestParam MultipartFile vphoto,
            @RequestParam String vdesc
    ) {
        try {
            ResultSet rs = DbLoader.executeSQL("select * from vendor where vemail='" + vemail + "'");
            if (rs.next()) {
                return "exists";
            } else {
                String projectPath = System.getProperty("user.dir");
                String internal_path = "/src/main/resources/static";
                String folderName = "/myUploads";
                String orgName = "/" + vphoto.getOriginalFilename();

                rs.moveToInsertRow();
                rs.updateString("vname", vname);
                rs.updateString("vemail", vemail);
                rs.updateInt("vcity", Integer.parseInt(vcity));
                rs.updateString("vpass", vpass);
                rs.updateInt("vservice", Integer.parseInt(vservice));
                rs.updateString("vsubservice", vsubservice);
                rs.updateString("vstart-time", vstartTime);
                rs.updateString("vend-time", vendTime);
                rs.updateString("vprice", vprice);
                rs.updateString("vcontact", vcontact);
                rs.updateString("vphoto", orgName);
                rs.updateString("vdesc", vdesc);
                rs.updateString("status", "blocked");
                rs.insertRow();

                byte b1[] = vphoto.getBytes();
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

    @PostMapping("/AdminShowVendors")
    public String showVendors() {
        try {
            String sql = "SELECT * FROM vendor";
            RDBMS_TO_JSON rdbms_to_json = new RDBMS_TO_JSON();
            String jsonResult = rdbms_to_json.generateJSON(sql);
            return jsonResult;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
    }

    @PostMapping("/approveVendors")
    public String approveVendors(@RequestParam String vid) {
        try {
            String sql = "select * from vendor where vid = " + vid + " ";
            ResultSet rs = DbLoader.executeSQL(sql);
            if (rs.next()) {
                rs.updateString("status", "approved");
                rs.updateRow();

                return "success";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
        return null;
    }

    @PostMapping("/blockVendors")
    public String blockVendors(@RequestParam String vid) {
        try {
            String sql = "select * from vendor where vid = " + vid + " ";
            ResultSet rs = DbLoader.executeSQL(sql);
            if (rs.next()) {
                rs.updateString("status", "blocked");
                rs.updateRow();

                return "success";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
        return null;
    }

    @PostMapping("/VendorLogin")
    public String login(@RequestParam String email, @RequestParam String pass, HttpSession session) {
        try {
            ResultSet rs = DbLoader.executeSQL("select * from vendor where vemail='" + email + "' and vpass='" + pass + "'");
            if (rs.next()) {
                session.setAttribute("vid", rs.getInt("vid"));
                return "success";
            } else {
                return "failed";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
    }

    @PostMapping("/VendorManagePhotos")
    public String photoDetails(@RequestParam MultipartFile p, @RequestParam String pd, HttpSession session) {
        try {
            ResultSet rs = DbLoader.executeSQL("select * from photos");
            String projectPath = System.getProperty("user.dir");
            String internal_path = "/src/main/resources/static";
            String folderName = "/myUploads";
            String orgName = "/" + p.getOriginalFilename();
            Integer id = (Integer) session.getAttribute("vid");
            rs.moveToInsertRow();
            rs.updateString("photo", orgName);
            rs.updateString("pdesc", pd);
            rs.updateInt("vid", id);
            rs.insertRow();

            byte b1[] = p.getBytes();
            FileOutputStream fos = new FileOutputStream(projectPath + internal_path + folderName + orgName);
            fos.write(b1);
            fos.close();

            return "success";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
    }

    @PostMapping("/AdminShowPhotos")
    public String showPhotos() {
        try {
            String sql = "SELECT * FROM photos";
            RDBMS_TO_JSON rdbms = new RDBMS_TO_JSON();
            String ans = rdbms.generateJSON(sql);
            return ans;
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"ans\":[]}";
        }
    }

    
    @PostMapping("/DeletePhoto")
    public String deletePhoto(@RequestParam int pid) {
        try {
            ResultSet rs = DbLoader.executeSQL("select * from photos where pid='" + pid + "'");
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
