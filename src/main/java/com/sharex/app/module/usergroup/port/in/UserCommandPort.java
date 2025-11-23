package com.sharex.app.module.usergroup.port.in;

import com.sharex.app.module.usergroup.application.command.CreateUserCommand;

public interface UserCommandPort {
    String createUser(CreateUserCommand command);
}
