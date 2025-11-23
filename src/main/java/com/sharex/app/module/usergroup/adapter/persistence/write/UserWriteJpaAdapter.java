package com.sharex.app.module.usergroup.adapter.persistence.write;

import com.sharex.app.infrastructure.persistence.write.UserWriteEntity;
import com.sharex.app.module.usergroup.port.out.UserWriteRepositoryPort;
import com.sharex.app.module.usergroup.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserWriteJpaAdapter implements UserWriteRepositoryPort {

    private final UserWriteJpaRepository repo;

    public UserWriteJpaAdapter(UserWriteJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    public void save(User user) {

        UserWriteEntity e = new UserWriteEntity();
        e.setId(user.getUserId());
        e.setName(user.getName());
        e.setEmail(user.getEmail());

        repo.save(e);
    }
}

