package com.sharex.app.module.user.adapter.persistence.read;

import com.sharex.app.module.user.application.dto.UserView;
import com.sharex.app.module.user.port.out.UserReadRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserReadRepositoryAdapter implements UserReadRepositoryPort {

    private final UserReadRepository repo;

    public UserReadRepositoryAdapter(UserReadRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<UserView> findById(String id) {
        return repo.findById(id)
                .map(e -> new UserView(e.getId(), e.getName(), e.getEmail()));
    }
}

