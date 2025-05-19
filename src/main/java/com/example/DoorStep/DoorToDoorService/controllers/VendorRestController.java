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
                rs.updateString("vstart_time", vstartTime);
                rs.updateString("vend_time", vendTime);
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
                session.setAttribute("vemail", email);
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

    @PostMapping("/getEditData")
    public String getEditData(HttpSession session) {
        int vid = (int) session.getAttribute("vid");

        System.out.println("VID");
        System.out.println(vid);

        String ans = new RDBMS_TO_JSON().generateJSON("select * from vendor where vid =" + vid + " ");

        return ans;
    }

    @PostMapping("/updateDetails")
    public String updateDetails(@RequestParam String vname,
            @RequestParam String vstartTime,
            @RequestParam String vendTime,
            @RequestParam String vprice,
            @RequestParam String vcontact,
            @RequestParam String vdesc,
            HttpSession session
    ) {
        try {
            int vid = (int) session.getAttribute("vid");
            ResultSet rs = DbLoader.executeSQL("select * from vendor where vid='" + vid + "'");
            if (rs.next()) {  // Move to the first row
                rs.updateString("vname", vname);
                rs.updateString("vstart_time", vstartTime);
                rs.updateString("vend_time", vendTime);
                rs.updateString("vprice", vprice);
                rs.updateString("vcontact", vcontact);
                rs.updateString("vdesc", vdesc);
                rs.updateRow();  // Commit the update to the DB
                return "success";
            } else {
                return "not_found";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
    }

    @PostMapping("/showBookings")
    public String showBookings(HttpSession session) {

        try {
            String vemail = (String) session.getAttribute("vemail");
            String ans = new RDBMS_TO_JSON().generateJSON(
                    "SELECT u.uname, u.uaddress, u.ucontact, u.uemail, "
                    + "b.booking_id, b.date, b.vendor_email, b.total_price, b.payment_type, b.status "
                    + "FROM booking b "
                    + "JOIN user u ON b.uid = u.uid "
                    + "WHERE b.vendor_email = '" + vemail + "'"
            );
            return ans;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
    }
    
    @PostMapping("/approveBooking")
    public String approveBooking(@RequestParam String bid) {
        try {
            String sql = "select * from booking where booking_id = " + bid + " ";
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

    @PostMapping("/blockBooking")
    public String blockBooking(@RequestParam String bid) {
        try {
            String sql = "select * from booking where booking_id = " + bid + " ";
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
    
    @PostMapping("/showSlots")
    public String showSlots(@RequestParam String bid, HttpSession session) {

        try {
            String ans = new RDBMS_TO_JSON().generateJSON("select * from booking_detail where booking_id = '" + bid + "'");
            return ans;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
    }
}
