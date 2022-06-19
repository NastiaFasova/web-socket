package com.example.kpi.socialnetwork.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private Long postId;

    @JsonCreator
    public CommentDto(@JsonProperty("id") Long id,@JsonProperty("postId") Long postId) {
        this.id = id;
        this.postId = postId;
    }
}
