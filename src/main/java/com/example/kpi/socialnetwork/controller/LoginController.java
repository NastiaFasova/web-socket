package com.example.kpi.socialnetwork.controller;

import com.example.kpi.socialnetwork.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String showNewLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

}
