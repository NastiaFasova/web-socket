package com.example.kpi.socialnetwork.service.impl;

import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.service.RegistrationService;
import com.example.kpi.socialnetwork.service.UserService;
import com.example.kpi.socialnetwork.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Implementation of service methods registration service
 * */
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registering a new user
     * */
    @Override
    public User register(User user) {
        User registeredUser = User.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .background(user.getBackground())
                .avatar(user.getAvatar())
                .password(passwordEncoder.encode(user.getPassword()))
                .build();
        return userService.save(registeredUser);
    }
}
