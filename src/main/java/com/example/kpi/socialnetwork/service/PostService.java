package com.example.kpi.socialnetwork.service;

import com.example.kpi.socialnetwork.common.UserPost;
import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    UserPost savePost(String email, Post post);

    UserPost createPost(String content, MultipartFile file) throws IOException;

    List<UserPost> getPostsOfUser(Long userId);

    List<UserPost> getAllPosts();

    UserPost findById(Long postId);

    List<UserPost> getSavedPostsOfUser(String email);

    List<UserPost> getLikedPostsOfUser(String email);

    boolean deletePost(Long postId);

    Post editPost(long postId, String newContent, MultipartFile file);
}
