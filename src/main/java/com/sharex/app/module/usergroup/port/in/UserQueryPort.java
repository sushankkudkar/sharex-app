package com.sharex.app.module.usergroup.port.in;

import com.sharex.app.module.usergroup.application.dto.UserView;

public interface UserQueryPort {
    UserView getUser(String id);
}
