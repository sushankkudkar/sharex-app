package com.sharex.app.module.usergroup.application.dto;

import com.sharex.app.module.usergroup.domain.User;

public record UserView(
        String id,
        String name,
        String email
) {
    public static UserView fromDomain(User user) {
        return new UserView(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
