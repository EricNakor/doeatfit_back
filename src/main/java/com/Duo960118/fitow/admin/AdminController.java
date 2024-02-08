package com.Duo960118.fitow.admin;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping(value = "/admin")
    public String admin(HttpServletRequest req) {
        req.setAttribute("contentPage", "/admin/admin.html");
        return "/admin/adminIndex";
    }
    @GetMapping(value = "/admin/manageUser")
    public String manageUser(HttpServletRequest req) {
        req.setAttribute("contentPage", "/admin/manageUser.html");
        return "/admin/adminIndex";
    }
    @GetMapping(value = "/admin/manageContact")
    public String manageContact(HttpServletRequest req) {
        req.setAttribute("contentPage", "/admin/manageContact.html");
        return "/admin/adminIndex";
    }
    @GetMapping(value = "/admin/manageHome")
    public String manageHome(HttpServletRequest req) {
        req.setAttribute("contentPage", "/admin/manageHome.html");
        return "/admin/adminIndex";
    }
    @GetMapping(value = "/admin/manageNotice")
    public String manageNotice(HttpServletRequest req) {
        req.setAttribute("contentPage", "/admin/manageNotice.html");
        return "/admin/adminIndex";
    }
    @GetMapping(value = "/admin/manageWorkOut")
    public String manageWorkOut(HttpServletRequest req) {
        req.setAttribute("contentPage", "/admin/manageWorkOut.html");
        return "/admin/adminIndex";
    }

}
