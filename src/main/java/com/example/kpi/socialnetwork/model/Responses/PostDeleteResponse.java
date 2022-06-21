package com.example.kpi.socialnetwork.model.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostDeleteResponse {
    private Long postId;
    private boolean success;
}
