package com.example.kpi.socialnetwork.model.json;

import com.example.kpi.socialnetwork.common.UserPost;
import com.example.kpi.socialnetwork.model.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostLight {
    private Long id;
    private String image;
    private String content;
    private String createdTime;
    private UserLight author;

    public PostLight(Post post, UserLight user) {
        author = user;
        id = post.getId();
        image = post.getImage();
        content = post.getContent();
        if (post.getCreatedTime() != null)
        {
            createdTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(post.getCreatedTime());
        }
    }

    public PostLight(UserPost post) {
        author = new UserLight(post.getAuthor());
        id = post.getId();
        image = post.getImage();
        content = post.getContent();
        if (post.getCreatedTime() != null)
        {
            createdTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(post.getCreatedTime());
        }
    }

    @Transient
    public String getImagePath()
    {
        if (id == null || image == null || image.isEmpty() || image.isBlank())
        {
            return "";
        }
        return String.format("~/user-photos/posts/%d/%s", id, image);
    }
}
