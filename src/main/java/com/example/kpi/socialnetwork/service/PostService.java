package com.example.kpi.socialnetwork.service;

import com.example.kpi.socialnetwork.model.Post;

import java.util.List;

public interface PostService {
    Post savePost(String email, Post post);

    List<Post> getPostsOfUser(Long userId);

    List<Post> getAllPosts();

    Post findById(Long postId);

    List<Post> getSavedPostsOfUser(String email);

    List<Post> getLikedPostsOfUser(String email);
}
