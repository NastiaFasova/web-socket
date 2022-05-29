package com.example.kpi.socialnetwork.controller;

import com.example.kpi.socialnetwork.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showNewLoginForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "login";
    }
}
