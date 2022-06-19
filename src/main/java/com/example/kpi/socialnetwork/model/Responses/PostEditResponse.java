package com.example.kpi.socialnetwork.model.Responses;

import com.example.kpi.socialnetwork.model.dto.PostEditDto;
import lombok.Data;

@Data
public class PostEditResponse {
    private Long postId;
    private String image;
    private String content;

    public PostEditResponse(PostEditDto postDto) {
        postId = postDto.getPostId();
        content = postDto.getContent();
        if (postDto.getImage() != null && !postDto.getImage().isEmpty())
        {
            image = String.format("/user-photos/posts/%d/%s", postId, postDto.getImage());
        }
    }
}
