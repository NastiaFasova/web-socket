package com.example.kpi.socialnetwork.service.impl;

import com.example.kpi.socialnetwork.model.Like;
import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.repository.PostRepository;
import com.example.kpi.socialnetwork.repository.UserRepository;
import com.example.kpi.socialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
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
    public List<User> findAllExceptCurrent() {
        var current = getLoggedInUser();
        return findAll().stream().filter(user -> !user.getId().equals(current.getId())).collect(Collectors.toList());
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
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

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User savePost(User user, Long postId) {
        Post originalPost = postRepository.getById(postId);
        if (user.getSaved().stream().anyMatch(s -> s.getId().equals(originalPost.getId())))
        {
            user.getSaved().remove(originalPost);
            userRepository.save(user);
            return null;
        }
        user.getSaved().add(originalPost);
        return userRepository.save(user);
    }
}
