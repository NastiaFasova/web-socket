package com.example.kpi.socialnetwork.model.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentReponse extends HtmlResponse{
    private Long postId;

    public CommentReponse(String html, Long postId) {
        super(html);
        this.postId = postId;
    }
}
