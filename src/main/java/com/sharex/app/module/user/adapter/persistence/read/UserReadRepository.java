package com.sharex.app.module.user.adapter.persistence.read;

import com.sharex.app.infrastructure.persistence.read.UserReadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReadRepository extends JpaRepository<UserReadEntity, String> {}
