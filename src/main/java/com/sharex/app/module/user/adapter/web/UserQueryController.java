package com.sharex.app.module.user.adapter.web;

import com.sharex.app.module.user.application.dto.UserView;
import com.sharex.app.shared.web.dto.ApiResponse;
import org.springframework.web.bind.annotation.*;
import com.sharex.app.module.user.port.in.UserQueryPort;

@RestController
@RequestMapping("/api/users")
public class UserQueryController {

    private final UserQueryPort queryPort;

    public UserQueryController(UserQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    @GetMapping("/{id}")
    public ApiResponse<UserView> getUser(@PathVariable String id) {
        UserView view = queryPort.getUser(id);
        return ApiResponse.success(view);
    }
}
