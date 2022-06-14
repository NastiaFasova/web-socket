package com.example.kpi.socialnetwork.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String image;
    private String content;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, fallbackPatterns = { "yyyy-MM-dd HH:mm" })
    private LocalDateTime createdTime;
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments;
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Like> likes;

    @Transient
    public String getImagePath()
    {
        if (id == null || image == null || image.isEmpty() || image.isBlank())
        {
            return "";
        }
        return String.format("user-photos/posts/%d/%s", id, image);
    }
}
