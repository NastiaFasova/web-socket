package com.example.kpi.socialnetwork.service;

import com.example.kpi.socialnetwork.model.User;

public interface UserService {
    User save(User user);

    User getByEmail(String email);

    User getLoggedInUser();
}
