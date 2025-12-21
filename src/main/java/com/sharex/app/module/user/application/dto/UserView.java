package com.sharex.app.module.user.application.dto;

import com.sharex.app.module.user.domain.User;

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
