package com.sharex.app.module.usergroup.application.command.handler;

import com.sharex.app.infrastructure.messaging.outbox.domain.DomainEvent;
import com.sharex.app.module.usergroup.application.command.CreateUserCommand;
import com.sharex.app.module.usergroup.domain.User;
import com.sharex.app.module.usergroup.port.in.UserCommandPort;
import com.sharex.app.module.usergroup.port.out.UserWriteRepositoryPort;
import com.sharex.app.shared.event.EventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CreateUserCommandHandler implements UserCommandPort {

    private final UserWriteRepositoryPort writeRepo;
    private final EventPublisher eventPublisher; // now supports batch

    public CreateUserCommandHandler(UserWriteRepositoryPort writeRepo,
                                    EventPublisher eventPublisher) {
        this.writeRepo = writeRepo;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public String createUser(CreateUserCommand command) {

        String id = UUID.randomUUID().toString();

        User user = new User(
                id,
                command.name(),
                command.email()
        );

        // Save domain model
        writeRepo.save(user);

        // Collect domain events
        List<DomainEvent> events = user.getEvents();

        // 🚀 Use batch insert for massive performance gain
        eventPublisher.publishBatch(events);

        return id;
    }
}
