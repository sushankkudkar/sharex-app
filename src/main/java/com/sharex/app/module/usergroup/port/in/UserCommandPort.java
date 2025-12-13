package com.sharex.app.module.usergroup.port.in;

import com.sharex.app.module.usergroup.application.command.CreateUserCommand;
import com.sharex.app.module.usergroup.application.dto.UserView;

public interface UserCommandPort {
    UserView createUser(CreateUserCommand command);
}
