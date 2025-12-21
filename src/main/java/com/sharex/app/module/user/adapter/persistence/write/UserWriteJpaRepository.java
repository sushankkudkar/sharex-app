package com.sharex.app.module.user.adapter.persistence.write;

import com.sharex.app.infrastructure.persistence.write.UserWriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWriteJpaRepository extends JpaRepository<UserWriteEntity, String> {
    boolean existsByEmail(String email);
}
