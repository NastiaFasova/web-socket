package com.example.kpi.socialnetwork.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PostDto {
    private Long id;

    @JsonCreator
    public PostDto(@JsonProperty("id") Long id) {
        this.id = id;
    }

    public PostDto() {
    }
}
