package com.example.kpi.socialnetwork.service.impl;

import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.repository.PostRepository;
import com.example.kpi.socialnetwork.repository.UserRepository;
import com.example.kpi.socialnetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of service methods for a User Entity
 * */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    /**
     * Saving user into DB
     * */
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Retrieving user from DB by email
     * */
    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Retrieving current logged in user from Security Principal
     * */
    @Override
    public User getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getByEmail(((UserDetails) principal).getUsername());
    }

    /**
     * Retrieving all users except current logged in
     * */
    @Override
    public List<User> findAllExceptCurrent() {
        var current = getLoggedInUser();
        return findAll().stream().filter(user -> !user.getId().equals(current.getId())).collect(Collectors.toList());
    }

    /**
     * Retrieving a user by ID
     * */
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    /**
     * Retrieve authors of posts
     * @param posts is the list of posts
     * */
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

    /**
     * Retrieve all users
     * */
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Saving post into user profile
     * */
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
