package com.sharex.app.module.usergroup.application.command.handler;

import com.sharex.app.module.usergroup.application.command.CreateUserCommand;
import com.sharex.app.module.usergroup.domain.User;
import com.sharex.app.module.usergroup.port.in.UserCommandPort;
import com.sharex.app.module.usergroup.port.out.UserWriteRepositoryPort;
import com.sharex.app.shared.event.EventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateUserCommandHandler implements UserCommandPort {

    private final UserWriteRepositoryPort writeRepo;
    private final EventPublisher eventPublisher;

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

        writeRepo.save(user);

        // Publish domain events
        user.getEvents().forEach(eventPublisher::publish);

        return id;
    }
}
