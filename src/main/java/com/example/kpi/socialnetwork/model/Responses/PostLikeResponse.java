package com.example.kpi.socialnetwork.model.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostLikeResponse {
    private boolean isLiked;
    private Long postId;
}
