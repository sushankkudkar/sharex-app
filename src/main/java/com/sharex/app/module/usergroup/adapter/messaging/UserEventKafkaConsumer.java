package com.sharex.app.module.usergroup.adapter.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharex.app.infrastructure.persistence.read.UserReadEntity;
import com.sharex.app.module.usergroup.adapter.persistence.read.UserReadRepository;
import com.sharex.app.module.usergroup.domain.event.UserCreatedEvent;
import com.sharex.app.infrastructure.persistence.outbox.AppliedEventEntity;
import com.sharex.app.infrastructure.persistence.outbox.AppliedEventRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserEventKafkaConsumer {

    private final ObjectMapper mapper;
    private final UserReadRepository repo;
    private final AppliedEventRepository appliedRepo;

    public UserEventKafkaConsumer(ObjectMapper mapper,
                                  UserReadRepository repo,
                                  AppliedEventRepository appliedRepo) {
        this.mapper = mapper;
        this.repo = repo;
        this.appliedRepo = appliedRepo;
    }

    @KafkaListener(
            topics = "user.events.user.created.v1",
            groupId = "cg.usergroup.read-model"
    )
    @Transactional
    public void consume(String payload) throws Exception {
        // deserialize to event type
        UserCreatedEvent event = mapper.readValue(payload, UserCreatedEvent.class);

        // idempotency check
        if (appliedRepo.existsById(event.eventId())) {
            System.out.println("[IDEMPOTENCY] Skipping event: " + event.eventId());
            return;
        }

        // apply projection
        UserReadEntity view = new UserReadEntity();
        view.setId(event.userId());
        view.setName(event.name());
        view.setEmail(event.email());

        repo.save(view);

        // mark applied
        appliedRepo.save(new AppliedEventEntity(event.eventId(), event.eventType()));

        System.out.println("[CONSUMER] Applied event: " + event.eventId());
    }
}
