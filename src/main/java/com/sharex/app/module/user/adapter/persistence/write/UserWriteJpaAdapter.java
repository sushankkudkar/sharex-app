package com.sharex.app.module.user.adapter.persistence.write;

import com.sharex.app.infrastructure.persistence.write.UserWriteEntity;
import com.sharex.app.module.user.port.out.UserWriteRepositoryPort;
import com.sharex.app.module.user.domain.User;
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
        e.setId(user.getId());
        e.setName(user.getName());
        e.setEmail(user.getEmail());

        repo.save(e);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repo.existsByEmail(email);
    }
}

