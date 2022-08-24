package com.example.kpi.socialnetwork.common;

import com.example.kpi.socialnetwork.model.User;
import lombok.Getter;
import lombok.Setter;

/**
 * Dto Entity for a user`s friend
 * */
@Getter
@Setter
public class UserFollow {
    private User user;
    private boolean isFollowed;
    private long followersCount;

    public UserFollow(User user, boolean isFollowed, long followersCount) {
        this.user = user;
        this.isFollowed = isFollowed;
        this.followersCount = followersCount;
    }
}
