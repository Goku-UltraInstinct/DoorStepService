package com.example.DoorStep.DoorToDoorService.controllers;

import com.example.DoorStep.DoorToDoorService.vmm.DbLoader;
import com.example.DoorStep.DoorToDoorService.vmm.RDBMS_TO_JSON;
import jakarta.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.util.StringTokenizer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserRestController {

    @Autowired
    public EmailSenderService email;

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
                session.setAttribute("uemail",email );
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

    @PostMapping("/showServices")
    public String showServices(@RequestParam("cid") String cid) {
        try {
            System.out.println("Received cid: " + cid); // Optional: for logging/debugging
            int cityid = Integer.parseInt(cid); // Use the correct variable name
            String ans = new RDBMS_TO_JSON().generateJSON(
                    //  "SELECT DISTINCT services.* FROM services JOIN vendors ON v_service = services.service_id WHERE v_city = " + cityid + " AND v_status = 'Blocked'"
                    "SELECT DISTINCT s.sid, s.sname, s.sdesc, s.sphoto, "
                    + "MIN(v.vprice) AS minprice " + "FROM services s JOIN vendor v ON v.vservice = s.sid "
                    + "WHERE v.vcity = " + cityid + " AND v.status = 'approved' "
                    + "GROUP BY s.sid, s.sname, s.sdesc, s.sphoto"
            );
            return ans;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
    }

    @PostMapping("/showVendors")
    public String showVendors(@RequestParam("cid") String cid, @RequestParam("sid") String sid) {
        try {
            System.out.println("Received cid: " + cid); // Optional: for logging/debugging
            System.out.println("Received sid: " + sid); // Optional: for logging/debugging

            int cityid = Integer.parseInt(cid); // Use the correct variable name
            int serviceid = Integer.parseInt(sid); // Use the correct variable name

            String ans = new RDBMS_TO_JSON().generateJSON(
                    "SELECT v.vid, v.vname, v.vprice, v.vphoto, s.sname "
                    + "FROM vendor v "
                    + "JOIN services s ON v.vservice = s.sid "
                    + "WHERE v.vcity = " + cityid + " "
                    + "AND v.vservice = " + serviceid + " "
                    + "AND v.status = 'approved'"
            );
            return ans;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
    }

    @PostMapping("/showServiceProviderDetails")
    public String showVendorDetails(@RequestParam("vid") String vid) {
        try {
            System.out.println("Received vid: " + vid); // Optional: for logging/debugging

            int vendorid = Integer.parseInt(vid); // Use the correct variable name

            String ans = new RDBMS_TO_JSON().generateJSON("select * from vendor where vid = " + vendorid + "");
            return ans;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
    }

    @GetMapping("/view_slots")
    public String view_slots(@RequestParam String email, @RequestParam String date) {

        System.out.println(date);
        System.out.println(email);
        try {
            ResultSet rs = DbLoader.executeSQL("select * from vendor where vemail='" + email + "'");

            String start;
            String end;
            String slot;
            if (rs.next()) {
                start = rs.getString("vstart_time");
                end = rs.getString("vend_time");
                slot = rs.getString("vprice");

            } else {
                String err = "failed";
                return err;
            }
            int Start = Integer.parseInt(start);
            int End = Integer.parseInt(end);
            int Slot = Integer.parseInt(slot);
            JSONObject ans = new JSONObject();

            //Define JSONArray
            JSONArray arr = new JSONArray();
            for (int i = Start; i <= End; i++) {
                JSONObject row = new JSONObject();
                row.put("start_slot", Start);
                row.put("end_slot", ++Start);
                row.put("slot_amount", slot);

                ResultSet rs2 = DbLoader.executeSQL("select * from booking_detail where start_slot ='" + i + "' and booking_id in (select booking_id from booking where date=\'" + date + "\' and vendor_email =\'" + email + "\' ) ");
                if (rs2.next()) {
                    row.put("status", "Booked");
                } else {
                    row.put("status", "Available");
                }

                arr.add(row);
            }
            ans.put("ans", arr);
            System.out.println(ans.toString());
            return (ans.toJSONString());

        } catch (Exception e) {
            return e.toString();
        }

    }

    @GetMapping("/pay")
    public String payment(@RequestParam String date,
            @RequestParam String vendor_email,
            @RequestParam String amount,
            @RequestParam String slots,
            HttpSession session,
            @RequestParam String type,
            @RequestParam String status) {
        String ans = "";
        Integer uid = (Integer) session.getAttribute("uid");

        try {
            // 1. Insert into booking table
            ResultSet rs = DbLoader.executeSQL("SELECT * FROM booking");
            rs.moveToInsertRow();
            rs.updateString("date", date);
            rs.updateString("vendor_email", vendor_email);
            rs.updateInt("uid", uid);
            rs.updateString("total_price", amount);
            rs.updateString("payment_type", type);
            rs.updateString("status", status);
            rs.insertRow();

            // 2. Get inserted booking_id
            int booking_id = 0;
            ResultSet rs2 = DbLoader.executeSQL("SELECT MAX(booking_id) AS maxid FROM booking");
            if (rs2.next()) {
                booking_id = rs2.getInt("maxid");
            }

            // 3. Insert slots into booking_detail table
            StringTokenizer st = new StringTokenizer(slots, ",");
            while (st.hasMoreTokens()) {
                int start_slot = Integer.parseInt(st.nextToken());
                int end_slot = start_slot + 1;

                ResultSet rs3 = DbLoader.executeSQL("SELECT * FROM booking_detail");
                rs3.moveToInsertRow();
                rs3.updateInt("booking_id", booking_id); // fk to booking_id
                rs3.updateString("start_slot", String.valueOf(start_slot));
                rs3.updateString("end_slot", String.valueOf(end_slot));
                rs3.insertRow();
            }

            ans = "success";
            return ans;
        } catch (Exception ex) {
            return ex.toString();
        }
    }

    @PostMapping("/showBookingHistory")
    public String showBookingHistory(HttpSession session) {

        try {
            Integer uid = (Integer) session.getAttribute("uid");
            String ans = new RDBMS_TO_JSON().generateJSON("select * from booking where uid = '" + uid + "'");
            return ans;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
    }

    @GetMapping("/sendemail")
    public String sendemail() {
        this.email.sendSimpleEmail("hiteshkumar00248@gmail.com", "Hello Everyone this is email testing mode", "Email Testing");
        return "success";
    }
    
    @PostMapping("/UserChangePassword")
    public String changePassword(@RequestParam String oldpass,
            @RequestParam String newpass,
            @RequestParam String confirmpass,
            HttpSession session) {
        try {
            String uemail = (String) session.getAttribute("uemail");
            if (uemail == null) {
                return "nosession";
            }

            ResultSet rs = DbLoader.executeSQL("select * from user where uemail='" + uemail + "' and upass='" + oldpass + "'");
            if (rs.next()) {
                if (oldpass.equals(newpass)) {
                    return "sameold";
                }
                if (!newpass.equals(confirmpass)) {
                    return "mismatch";
                }

                rs.updateString("upass", newpass);
                rs.updateRow();  // ✅ important step
                return "success";
            } else {
                return "wrongold";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "exception";
        }
    }
    
    @PostMapping("/DirectToChangePassword")
    public String changePassword(@RequestParam String email, @RequestParam String pass, HttpSession session) {
        try {
            ResultSet rs = DbLoader.executeSQL("select * from user where uemail='" + email + "' and upass='" + pass + "'");
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
