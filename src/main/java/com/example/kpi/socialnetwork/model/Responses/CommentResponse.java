package com.example.kpi.socialnetwork.model.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponse extends HtmlResponse{
    private Long postId;

    public CommentResponse(String html, Long postId) {
        super(html);
        this.postId = postId;
    }
}
