package com.example.kpi.socialnetwork.model.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultResponse {
    private boolean success;
    private Long postId;
}
