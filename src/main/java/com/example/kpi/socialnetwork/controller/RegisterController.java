package com.example.kpi.socialnetwork.controller;

import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/register")
public class RegisterController {
    private final RegistrationService registerService;

    @Autowired
    public RegisterController(RegistrationService registerService) {
        this.registerService = registerService;
    }

    @PostMapping
    public String register(@ModelAttribute("formData") User user,
                           BindingResult bindingResult) {
        if (bindingResult != null && bindingResult.hasErrors()) {
            return "register";
        }
        registerService.register(user);
        return "redirect:/login";
    }

    @GetMapping
    public String showNewRegisterForm(Model model) {
        User user = new User();
        model.addAttribute("formData", user);
        return "register";
    }
}
