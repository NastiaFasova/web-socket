package com.example.kpi.socialnetwork.service.impl;

import com.example.kpi.socialnetwork.exceptions.LikeException;
import com.example.kpi.socialnetwork.model.Comment;
import com.example.kpi.socialnetwork.model.Like;
import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.repository.CommentRepository;
import com.example.kpi.socialnetwork.repository.LikeRepository;
import com.example.kpi.socialnetwork.repository.PostRepository;
import com.example.kpi.socialnetwork.repository.UserRepository;
import com.example.kpi.socialnetwork.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of service methods for Like Entity
 * */
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    /**
     * Add like to post
     * @param postId is an ID of the post user want to like
     * @param loggedInUserId is an ID of user
     * @return Like Entity
     * @exception LikeException is if user not retrieved
     * */
    @Override
    public Like addLikeToPost(Long postId, Long loggedInUserId) {
        Post post = postRepository.findById(postId).orElse(null);
        User user = userRepository.findByIdFetchLikes(loggedInUserId).orElse(null);
        if (post != null && user != null) {
            //retrieve user by Id
            Like likeByUserAndPost = post.getLikes().stream()
                    .filter(l -> l.getUser().getId().equals(loggedInUserId))
                    .findAny()
                    .orElse(null);

            //add like if it`s absent
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
            //otherwise remove like
            post.getLikes().remove(likeByUserAndPost);
            postRepository.save(post);
            user.getLikes().remove(post);
            userRepository.save(user);
            likeRepository.delete(likeByUserAndPost);
            return null;
        }
        throw new LikeException("Could not retrieve user or post by ID");
    }

    /**
     * Add like to comment
     * @param commentId is an ID of the comment user want to like
     * @param loggedInUserId is an ID of user
     * @return Like Entity
     * @exception LikeException is if user not retrieved
     * */
    @Override
    public Like addLikeToComment(Long commentId, Long loggedInUserId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
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

            comment.getLikes().remove(likeByUserAndPost);
            commentRepository.save(comment);
            likeRepository.delete(likeByUserAndPost);
            return null;
        }
        throw new LikeException("Could not retrieve user or post by ID");
    }
}
