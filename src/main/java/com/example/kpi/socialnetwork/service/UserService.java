package com.example.kpi.socialnetwork.service;

import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;

import java.util.List;

public interface UserService {
    User save(User user);

    User getByEmail(String email);

    User getLoggedInUser();

    User getUserById(Long userId);

    List<User> getAuthors(List<Post> posts);

    List<User> findAll();

    User savePost(User user, Long postId);

    User retweetPost(User loggedInUser, Long postId);

    User deletePost(User loggedInUser, Long postId);
}
