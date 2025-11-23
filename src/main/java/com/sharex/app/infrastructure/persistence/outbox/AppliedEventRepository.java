package com.sharex.app.infrastructure.persistence.outbox;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppliedEventRepository extends JpaRepository<AppliedEventEntity, String> {
    // existsById is provided by JpaRepository
}
