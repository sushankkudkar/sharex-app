package com.sharex.app.module.usergroup.adapter.web;

import com.sharex.app.module.usergroup.application.dto.UserView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sharex.app.module.usergroup.port.in.UserQueryPort;

@RestController
@RequestMapping("/api/users")
public class UserQueryController {

    private final UserQueryPort queryPort;

    public UserQueryController(UserQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    @GetMapping("/{id}")
    public UserView getUser(@PathVariable String id) {
        return queryPort.getUser(id);
    }
}
