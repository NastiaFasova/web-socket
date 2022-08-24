package com.example.kpi.socialnetwork.service;

import com.example.kpi.socialnetwork.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Service layer for Registration purpose
 * */
public interface RegistrationService {
    User register(User user);
}
