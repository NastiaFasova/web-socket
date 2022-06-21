package com.example.kpi.socialnetwork.model.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse extends HtmlResponse {
    private Long authorId;
    private Long postId;

    public PostResponse(String html, Long authorId, Long postId) {
        super(html);
        this.authorId = authorId;
        this.postId = postId;
    }
}
