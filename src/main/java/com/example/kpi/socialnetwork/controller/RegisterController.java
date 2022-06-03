package com.example.kpi.socialnetwork.controller;

import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.model.dto.UserRegisterDto;
import com.example.kpi.socialnetwork.model.mapper.UserRegisterMapper;
import com.example.kpi.socialnetwork.service.RegistrationService;
import com.example.kpi.socialnetwork.service.UserService;
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

import javax.validation.Valid;
import java.io.IOException;


@Controller
@RequestMapping("/register")
public class RegisterController {
    private final RegistrationService registerService;
    private final UserService userService;
    private final UserRegisterMapper userRegisterMapper;

    @Autowired
    public RegisterController(RegistrationService registerService, UserService userService,
                              UserRegisterMapper userRegisterMapper) {
        this.registerService = registerService;
        this.userService = userService;
        this.userRegisterMapper = userRegisterMapper;
    }

    @PostMapping
    public String register(@Valid @ModelAttribute("formData") UserRegisterDto userRegisterDto,
                           BindingResult bindingResult,
                           @RequestParam("fileImage") MultipartFile avatar)
            throws IOException {
        if (bindingResult != null && bindingResult.hasErrors()) {
            return "register";
        }
        if (avatar.getOriginalFilename() == null || avatar.getOriginalFilename().isEmpty()) {
            registerService.register(userRegisterMapper.getUser(userRegisterDto));
            return "redirect:/login";
        }
        String fileName = StringUtils.cleanPath(avatar.getOriginalFilename());
        userRegisterDto.setAvatar(fileName);
        User registeredUser = registerService.register(userRegisterMapper.getUser(userRegisterDto));
        String uploadDir = "user-photos/avatar/" + registeredUser.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, avatar);
        return "redirect:/login";
    }

    @GetMapping
    public String showNewRegisterForm(Model model) {
        UserRegisterDto user = new UserRegisterDto();
        model.addAttribute("formData", user);
        return "register";
    }
}
