package com.example.kpi.socialnetwork.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private String confirmPassword;
    private String background;
    private String avatar;
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
    private String description;
    @ManyToMany
    private List<Post> posts;
    @ManyToMany
    private List<Post> likes;
    @ManyToMany
    private List<Post> saved;
    @ManyToMany
    private List<Post> retweeted;

    @Transient
    public String getPhotosImagePath() {
        if (background == null || id == null) {
            return "";
        }
        return "/user-photos/" + id + "/" + background;
    }

    @Transient
    public String getAvatarImagePath() {
        if (avatar == null || id == null) {
            return "";
        }
        return "/user-photos/avatar/" + id + "/" + avatar;
    }
}
