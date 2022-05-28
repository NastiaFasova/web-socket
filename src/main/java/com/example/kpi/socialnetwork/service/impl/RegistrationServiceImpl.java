package com.example.kpi.socialnetwork.service.impl;

import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.service.RegistrationService;
import com.example.kpi.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final UserService userService;

    @Autowired
    public RegistrationServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User register(User user) {
        User registeredUser = User.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .image(user.getImage())
                .password(user.getPassword())
                .build();
        return userService.save(registeredUser);
    }
}
