package com.example.kpi.socialnetwork.controller;

import com.example.kpi.socialnetwork.model.dto.UserLoginDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showNewLoginForm(Model model) {
        UserLoginDto user = new UserLoginDto();
        model.addAttribute("user", user);
        return "login";
    }
}
