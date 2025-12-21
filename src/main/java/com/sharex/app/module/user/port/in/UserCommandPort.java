package com.sharex.app.module.user.port.in;

import com.sharex.app.module.user.application.command.CreateUserCommand;
import com.sharex.app.module.user.application.dto.UserView;

public interface UserCommandPort {
    UserView createUser(CreateUserCommand command);
}
