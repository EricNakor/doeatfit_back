package com.Duo960118.fitow.workout;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WorkOutController {

    @GetMapping(value = "/workout")
    public String workout(HttpServletRequest req) {
        req.setAttribute("contentPage", "/workout/workout.html");
        return "index";
    }

}
