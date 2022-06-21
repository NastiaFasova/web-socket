package com.example.kpi.socialnetwork.model.json;

import com.example.kpi.socialnetwork.common.UserComment;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
public class CommentLight {
    private Long id;
    private UserLight author;
    private String localDateTime;
    private String content;

    public CommentLight(UserComment comment) {
        id = comment.getId();
        author = new UserLight(comment.getAuthor());
        content = comment.getContent();
        if (comment.getLocalDateTime() != null)
        {
            localDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(comment.getLocalDateTime());
        }
    }
}
