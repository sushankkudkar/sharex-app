package com.sharex.app.infrastructure.messaging.outbox.repo;

import com.sharex.app.infrastructure.persistence.outbox.entity.OutboxEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEventEntity, String> {
    List<OutboxEventEntity> findByPublishedFalse();
}