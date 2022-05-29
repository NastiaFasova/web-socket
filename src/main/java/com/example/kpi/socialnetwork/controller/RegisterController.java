package com.example.kpi.socialnetwork.controller;

import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.service.RegistrationService;
import com.example.kpi.socialnetwork.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


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
                           BindingResult bindingResult, @RequestParam("fileImage") MultipartFile multipartFile)
            throws IOException {
        if (bindingResult != null && bindingResult.hasErrors()) {
            return "register";
        }
        if (multipartFile.getOriginalFilename() == null || multipartFile.getOriginalFilename().isEmpty()) {
            registerService.register(user);
            return "redirect:/login";
        }
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        user.setImage(fileName);
        User registeredUser = registerService.register(user);
        String uploadDir = "user-photos/" + registeredUser.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        return "redirect:/login";
    }

    @GetMapping
    public String showNewRegisterForm(Model model) {
        User user = new User();
        model.addAttribute("formData", user);
        return "register";
    }
}
