package com.example.kpi.socialnetwork.common;

import com.example.kpi.socialnetwork.model.Like;
import com.example.kpi.socialnetwork.model.User;
import com.example.kpi.socialnetwork.model.Comment;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class UserComment {
    private Long id;
    private User author;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, fallbackPatterns = { "yyyy-MM-dd HH:mm" })
    private LocalDateTime localDateTime;
    private String content;
    private List<Like> likes;

    public UserComment(Comment comment, User author) {
        id = comment.getId();
        localDateTime = comment.getLocalDateTime();
        content = comment.getContent();
        likes = comment.getLikes();
        this.author = author;
    }
}
