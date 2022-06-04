package com.example.kpi.socialnetwork.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
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

    @Transient
    public String getPhotosImagePath() {
        if (background == null || id == null) {
            return null;
        }
        return "/user-photos/" + id + "/" + background;
    }

    @Transient
    public String getAvatarImagePath() {
        if (avatar == null || id == null) {
            return null;
        }
        return "/user-photos/" + id + "/avatar/" + avatar;
    }
}
