package com.sharex.app.module.usergroup.adapter.web;

import com.sharex.app.module.usergroup.application.command.CreateUserCommand;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sharex.app.module.usergroup.port.in.UserCommandPort;

@RestController
@RequestMapping("/api/users")
public class UserCommandController {

    private final UserCommandPort commandPort;

    public UserCommandController(UserCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    @PostMapping
    public String createUser(@Valid @RequestBody CreateUserCommand cmd) {
        return commandPort.createUser(cmd);
    }
}

