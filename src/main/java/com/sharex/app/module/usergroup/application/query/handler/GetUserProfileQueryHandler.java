package com.sharex.app.module.usergroup.application.query.handler;

import com.sharex.app.module.usergroup.application.dto.UserView;
import com.sharex.app.module.usergroup.domain.exception.UserNotFoundException;
import com.sharex.app.module.usergroup.port.in.UserQueryPort;
import com.sharex.app.module.usergroup.port.out.UserReadRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class GetUserProfileQueryHandler implements UserQueryPort {

    private final UserReadRepositoryPort readRepo;

    public GetUserProfileQueryHandler(UserReadRepositoryPort readRepo) {
        this.readRepo = readRepo;
    }

    @Override
    public UserView getUser(String id) {
        return readRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
