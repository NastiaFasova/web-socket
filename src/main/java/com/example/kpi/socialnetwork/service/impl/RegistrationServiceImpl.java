package com.example.kpi.socialnetwork.service.impl;

import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.service.RegistrationService;
import com.example.kpi.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationServiceImpl(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        User registeredUser = User.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .image(user.getImage())
                .password(passwordEncoder.encode(user.getPassword()))
                .build();
        return userService.save(registeredUser);
    }
}
