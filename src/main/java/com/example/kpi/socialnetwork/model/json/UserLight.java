package com.example.kpi.socialnetwork.model.json;

import com.example.kpi.socialnetwork.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import javax.persistence.Transient;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLight {
    private Long id;
    private String fullName;
    private String email;
    private String password;
    private String confirmPassword;
    private String background;
    private String avatar;
    private String phone;
    private String address;
    private String dateOfBirth;

    public UserLight(User user) {
        id = user.getId();
        fullName = user.getFullName();
        email = user.getEmail();
        password = user.getPassword();
        confirmPassword = user.getConfirmPassword();
        background = user.getBackground();
        avatar = user.getAvatar();
        phone = user.getPhone();
        address = user.getAddress();
        if (user.getDateOfBirth() != null)
        {
            dateOfBirth = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(user.getDateOfBirth());
        }
    }

    @Transient
    public String getPhotosImagePath() {
        if (background == null || id == null) {
            return "";
        }
        return "~/user-photos/panorama/" + id + "/" + background;
    }

    @Transient
    public String getAvatarImagePath() {
        if (avatar == null || id == null) {
            return "";
        }
        return "~/user-photos/avatar/" + id + "/" + avatar;
    }
}
