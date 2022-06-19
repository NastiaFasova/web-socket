package com.example.kpi.socialnetwork.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostEditDto {
    @JsonProperty("id")
    private Long postId;
    @JsonProperty("content")
    private String content;
    @JsonProperty("image")
    private String image;
}
