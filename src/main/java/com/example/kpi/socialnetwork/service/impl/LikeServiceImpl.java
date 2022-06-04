package com.example.kpi.socialnetwork.service.impl;

import com.example.kpi.socialnetwork.model.Comment;
import com.example.kpi.socialnetwork.model.Like;
import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.repository.CommentRepository;
import com.example.kpi.socialnetwork.repository.LikeRepository;
import com.example.kpi.socialnetwork.repository.PostRepository;
import com.example.kpi.socialnetwork.repository.UserRepository;
import com.example.kpi.socialnetwork.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
public class LikeServiceImpl implements LikeService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public LikeServiceImpl(PostRepository postRepository, LikeRepository likeRepository,
                           UserRepository userRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Like addLikeToPost(Long postId, Long loggedInUserId) {
        Post post = postRepository.findById(postId).orElse(null);
        User user = userRepository.findByIdFetchLikes(loggedInUserId).orElse(null);
        if (post != null && user != null) {
            Like likeByUserAndPost = post.getLikes().stream()
                    .filter(l -> l.getUser().getId().equals(loggedInUserId))
                    .findAny()
                    .orElse(null);

            if (likeByUserAndPost == null) {
                Like like = new Like();
                like.setUser(user);
                post.getLikes().add(like);
                likeRepository.save(like);
                postRepository.save(post);
                user.getLikes().add(post);
                userRepository.save(user);
                return like;
            }
            Iterator<Like> i = post.getLikes().iterator();
            while (i.hasNext()) {
                Like like = i.next();
                if (like.equals(likeByUserAndPost)) {
                    i.remove();
                    break;
                }
            }
            postRepository.save(post);
            likeRepository.delete(likeByUserAndPost);
            return likeByUserAndPost;
        }
        throw new RuntimeException();
    }

    @Override
    public Like addLikeToComment(Long postId, Long loggedInUserId) {
        Comment comment = commentRepository.findById(postId).orElse(null);
        User user = userRepository.findById(loggedInUserId).orElse(null);
        if (comment != null) {
            Like likeByUserAndPost = comment.getLikes().stream()
                    .filter(l -> l.getUser().getId().equals(loggedInUserId))
                    .findAny()
                    .orElse(null);

            if (likeByUserAndPost == null) {
                Like like = new Like();
                like.setUser(user);
                comment.getLikes().add(like);
                likeRepository.save(like);
                commentRepository.save(comment);
                return like;
            }
            Iterator<Like> i = comment.getLikes().iterator();
            while (i.hasNext()) {
                Like like = i.next();
                if (like.equals(likeByUserAndPost)) {
                    i.remove();
                    break;
                }
            }
            commentRepository.save(comment);
            likeRepository.delete(likeByUserAndPost);
            return likeByUserAndPost;
        }
        throw new RuntimeException();
    }
}
