package com.Duo960118.fitow.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping(value = "/")
    public String home(HttpServletRequest req) {
        req.setAttribute("contentPage", "home.html");
        return "index";
    }

}
