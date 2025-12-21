package com.sharex.app.module.user.adapter.web;

import com.sharex.app.module.user.application.command.CreateUserCommand;
import com.sharex.app.module.user.application.dto.UserView;
import com.sharex.app.shared.web.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sharex.app.module.user.port.in.UserCommandPort;

@RestController
@RequestMapping("/api/users")
public class UserCommandController {

    private final UserCommandPort commandPort;

    public UserCommandController(UserCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    @PostMapping
    public ApiResponse<UserView> createUser(@Valid @RequestBody CreateUserCommand cmd) {
        UserView view = commandPort.createUser(cmd);
        return ApiResponse.success(view);
    }
}

