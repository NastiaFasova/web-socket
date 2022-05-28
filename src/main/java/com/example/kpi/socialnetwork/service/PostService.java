package com.example.kpi.socialnetwork.service;

import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;

import java.util.List;

public interface PostService {
    Post savePost(User user, String content);

    List<Post> getPostsOfUser(Long userId);

    List<Post> getAllPosts();
}
