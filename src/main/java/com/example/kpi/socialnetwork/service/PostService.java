package com.example.kpi.socialnetwork.service;

import com.example.kpi.socialnetwork.common.KeyValuePair;
import com.example.kpi.socialnetwork.common.UserPost;
import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    Post savePost(String email, Post post);

    UserPost createPost(String content, MultipartFile file) throws IOException;

    List<Post> getPostsOfUser(Long userId);

    List<UserPost> getAllPosts();

    Post findById(Long postId);

    List<Post> getSavedPostsOfUser(String email);

    List<Post> getLikedPostsOfUser(String email);
}
