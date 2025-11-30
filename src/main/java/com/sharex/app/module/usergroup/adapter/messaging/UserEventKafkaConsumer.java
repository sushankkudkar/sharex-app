package com.sharex.app.module.usergroup.adapter.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharex.app.infrastructure.idempotency.RedisIdempotencyService;
import com.sharex.app.infrastructure.persistence.read.UserReadEntity;
import com.sharex.app.module.usergroup.adapter.persistence.read.UserReadRepository;
import com.sharex.app.module.usergroup.domain.event.UserCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserEventKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(UserEventKafkaConsumer.class);

    private final ObjectMapper mapper;
    private final UserReadRepository repo;
    private final RedisIdempotencyService idempotency;

    public UserEventKafkaConsumer(ObjectMapper mapper,
                                  UserReadRepository repo,
                                  RedisIdempotencyService idempotency) {
        this.mapper = mapper;
        this.repo = repo;
        this.idempotency = idempotency;
    }

    @KafkaListener(
            topics = "user.events.user.created.v1",
            groupId = "cg.usergroup.read-model"
    )
    @Transactional("readTransactionManager")
    public void consume(String payload) throws Exception {

        UserCreatedEvent event = mapper.readValue(payload, UserCreatedEvent.class);
        String eventId = event.eventId();

        // Redis idempotency check
        if (idempotency.isAlreadyProcessed(eventId)) {
            log.debug("[IDEMPOTENCY][REDIS] Skipping {}", eventId);
            return;
        }

        // Apply read model
        UserReadEntity view = new UserReadEntity();
        view.setId(event.aggregateId());
        view.setName(event.name());
        view.setEmail(event.email());
        repo.save(view);

        // Mark event processed in Redis
        idempotency.markProcessed(eventId);

        log.info("[CONSUMER] Applied event {}", eventId);
    }
}
