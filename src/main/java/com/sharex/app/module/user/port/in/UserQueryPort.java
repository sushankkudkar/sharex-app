package com.sharex.app.module.user.port.in;

import com.sharex.app.module.user.application.dto.UserView;

public interface UserQueryPort {
    UserView getUser(String id);
}
