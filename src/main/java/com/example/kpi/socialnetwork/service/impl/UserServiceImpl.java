package com.example.kpi.socialnetwork.service.impl;

import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.repository.UserRepository;
import com.example.kpi.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getByEmail(((UserDetails) principal).getUsername());
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.getById(userId);
    }

    @Override
    public List<User> getAuthors(List<Post> posts) {
        List<User> users = userRepository.findAll();
        List<User> authors = new ArrayList<>();
        for (User user : users) {
            for (Post post : posts) {
                for (Post userPost : user.getPosts()) {
                    if (userPost.equals(post)) {
                        authors.add(user);
                    }
                }
            }
        }
        return authors;
    }
}
