package com.example.kpi.socialnetwork.common;

import com.example.kpi.socialnetwork.model.Comment;
import com.example.kpi.socialnetwork.model.Like;
import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserPost {
    private Long id;
    private String image;
    private String content;
    private LocalDateTime createdTime;
    private List<Comment> comments;
    private List<Like> likes;
    private User author;
    private boolean isLiked;
    private boolean isRetweeted;
    private boolean isSaved;
    private Long savesCount;

    public UserPost(User author, Post post) {
        this.author = author;
        id = post.getId();
        image = post.getImage();
        createdTime = post.getCreatedTime();
        content = post.getContent();
        comments = new ArrayList<>();
        if (post.getComments() != null)
        {
            comments.addAll(post.getComments());
        }
        likes = new ArrayList<>();
        if (post.getLikes() != null)
        {
            likes.addAll(post.getLikes());
        }
    }

    @Transient
    public String getImagePath()
    {
        if (id == null || image == null || image.isEmpty() || image.isBlank())
        {
            return "";
        }
        return String.format("user-photos/posts/%d/%s", id, image);
    }

}
